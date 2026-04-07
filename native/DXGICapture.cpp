#include "DXGICapture.h"
#include <iostream>

namespace fastrobot {

DXGICapture::DXGICapture() 
    : captureX(0), captureY(0), captureWidth(0), captureHeight(0)
    , isCapturing(false), currentFPS(0.0), frameCount(0) {
}

DXGICapture::~DXGICapture() {
    Shutdown();
}

bool DXGICapture::Initialize(int x, int y, int width, int height) {
    captureX = x;
    captureY = y;
    captureWidth = width;
    captureHeight = height;

    // Create D3D11 device
    D3D_FEATURE_LEVEL featureLevels[] = { D3D_FEATURE_LEVEL_11_0 };
    D3D_FEATURE_LEVEL featureLevel;

    HRESULT hr = D3D11CreateDevice(
        nullptr,
        D3D_DRIVER_TYPE_HARDWARE,
        nullptr,
        0,
        featureLevels,
        1,
        D3D11_SDK_VERSION,
        &d3dDevice,
        &featureLevel,
        &d3dContext
    );

    if (FAILED(hr)) {
        std::cerr << "Failed to create D3D11 device: " << std::hex << hr << std::endl;
        return false;
    }

    // Get DXGI device
    hr = d3dDevice.As(&dxgiDevice);
    if (FAILED(hr)) {
        std::cerr << "Failed to get DXGI device: " << std::hex << hr << std::endl;
        return false;
    }

    // Get adapter
    hr = dxgiDevice->GetAdapter(&dxgiAdapter);
    if (FAILED(hr)) {
        std::cerr << "Failed to get DXGI adapter: " << std::hex << hr << std::endl;
        return false;
    }

    // Get primary output
    hr = dxgiAdapter->EnumOutputs(0, &dxgiOutput);
    if (FAILED(hr)) {
        std::cerr << "Failed to get DXGI output: " << std::hex << hr << std::endl;
        return false;
    }

    // Cast to IDXGIOutput1 for duplication
    hr = dxgiOutput.As(&dxgiOutput1);
    if (FAILED(hr)) {
        std::cerr << "Failed to get IDXGIOutput1: " << std::hex << hr << std::endl;
        return false;
    }

    // Create staging texture for CPU read access
    ZeroMemory(&stagingDesc, sizeof(stagingDesc));
    stagingDesc.Width = captureWidth;
    stagingDesc.Height = captureHeight;
    stagingDesc.MipLevels = 1;
    stagingDesc.ArraySize = 1;
    stagingDesc.Format = DXGI_FORMAT_B8G8R8A8_UNORM;
    stagingDesc.SampleDesc.Count = 1;
    stagingDesc.Usage = D3D11_USAGE_STAGING;
    stagingDesc.CPUAccessFlags = D3D11_CPU_ACCESS_READ;

    hr = d3dDevice->CreateTexture2D(&stagingDesc, nullptr, &stagingTexture);
    if (FAILED(hr)) {
        std::cerr << "Failed to create staging texture: " << std::hex << hr << std::endl;
        return false;
    }

    return true;
}

void DXGICapture::Shutdown() {
    StopCapture();
    desktopDuplication.Reset();
    dxgiOutput1.Reset();
    dxgiOutput.Reset();
    stagingTexture.Reset();
    dxgiAdapter.Reset();
    dxgiDevice.Reset();
    d3dContext.Reset();
    d3dDevice.Reset();
}

bool DXGICapture::StartCapture() {
    if (isCapturing) return true;

    // Create desktop duplication
    HRESULT hr = dxgiOutput1->DuplicateOutput(d3dDevice.Get(), &desktopDuplication);
    if (FAILED(hr)) {
        std::cerr << "Failed to create desktop duplication: " << std::hex << hr << std::endl;
        return false;
    }

    isCapturing = true;
    lastFrameTime = std::chrono::high_resolution_clock::now();
    frameCount = 0;

    return true;
}

void DXGICapture::StopCapture() {
    isCapturing = false;
    desktopDuplication.Reset();
    
    // Clear frame queue
    std::lock_guard<std::mutex> lock(queueMutex);
    while (!frameQueue.empty()) {
        frameQueue.pop();
    }
}

bool DXGICapture::HasNewFrame() {
    if (!isCapturing) return false;
    
    std::lock_guard<std::mutex> lock(queueMutex);
    return !frameQueue.empty();
}

FrameData DXGICapture::GetNextFrame() {
    std::lock_guard<std::mutex> lock(queueMutex);
    if (frameQueue.empty()) {
        return FrameData{};
    }
    
    FrameData frame = std::move(frameQueue.front());
    frameQueue.pop();
    return frame;
}

double DXGICapture::GetCurrentFPS() {
    return currentFPS.load();
}

bool DXGICapture::CaptureFrame() {
    if (!isCapturing || !desktopDuplication) return false;

    IDXGIResource* desktopResource = nullptr;
    DXGI_OUTDUPL_FRAME_INFO frameInfo;

    // Acquire next frame (timeout after 100ms)
    HRESULT hr = desktopDuplication->AcquireNextFrame(100, &frameInfo, &desktopResource);
    
    if (hr == DXGI_ERROR_WAIT_TIMEOUT) {
        // No new frame available
        return true;
    }
    
    if (FAILED(hr)) {
        std::cerr << "Failed to acquire frame: " << std::hex << hr << std::endl;
        return false;
    }

    bool success = ProcessFrame(desktopResource);

    desktopResource->Release();
    desktopDuplication->ReleaseFrame();

    // Calculate FPS
    auto now = std::chrono::high_resolution_clock::now();
    frameCount++;
    
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(now - lastFrameTime).count();
    if (duration >= 1000) {
        currentFPS.store(frameCount * 1000.0 / duration);
        frameCount = 0;
        lastFrameTime = now;
    }

    return success;
}

bool DXGICapture::ProcessFrame(IDXGIResource* desktopResource) {
    ComPtr<ID3D11Texture2D> desktopTexture;
    HRESULT hr = desktopResource->QueryInterface(IID_PPV_ARGS(&desktopTexture));
    if (FAILED(hr)) {
        return false;
    }

    // Copy from desktop texture to staging texture (region of interest)
    D3D11_BOX box;
    box.left = captureX;
    box.top = captureY;
    box.front = 0;
    box.right = captureX + captureWidth;
    box.bottom = captureY + captureHeight;
    box.back = 1;

    d3dContext->CopySubresourceRegion(
        stagingTexture.Get(), 0, 0, 0, 0,
        desktopTexture.Get(), 0, &box
    );

    // Map staging texture for CPU read
    D3D11_MAPPED_SUBRESOURCE mapped;
    hr = d3dContext->Map(stagingTexture.Get(), 0, D3D11_MAP_READ, 0, &mapped);
    if (FAILED(hr)) {
        return false;
    }

    // Copy to frame buffer
    FrameData frame;
    frame.width = captureWidth;
    frame.height = captureHeight;
    frame.timestamp = std::chrono::high_resolution_clock::now();
    frame.data.resize(captureWidth * captureHeight * 4);

    // Copy row by row (handle pitch)
    UINT8* dest = frame.data.data();
    UINT8* src = static_cast<UINT8*>(mapped.pData);
    
    for (int y = 0; y < captureHeight; y++) {
        memcpy(dest + y * captureWidth * 4, src + y * mapped.RowPitch, captureWidth * 4);
    }

    d3dContext->Unmap(stagingTexture.Get(), 0);

    // Add to queue (drop oldest if full)
    {
        std::lock_guard<std::mutex> lock(queueMutex);
        if (frameQueue.size() >= MAX_QUEUE_SIZE) {
            frameQueue.pop();
        }
        frameQueue.push(std::move(frame));
    }

    return true;
}

} // namespace fastrobot
