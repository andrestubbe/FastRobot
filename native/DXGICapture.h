#pragma once

#include <jni.h>
#include <d3d11.h>
#include <dxgi1_2.h>
#include <wrl/client.h>
#include <vector>
#include <queue>
#include <mutex>
#include <atomic>
#include <chrono>

using Microsoft::WRL::ComPtr;

namespace fastrobot {

struct FrameData {
    std::vector<UINT8> data;
    int width;
    int height;
    std::chrono::high_resolution_clock::time_point timestamp;
};

class DXGICapture {
public:
    DXGICapture();
    ~DXGICapture();

    bool Initialize(int x, int y, int width, int height);
    void Shutdown();
    
    bool StartCapture();
    void StopCapture();
    
    bool HasNewFrame();
    FrameData GetNextFrame();
    double GetCurrentFPS();
    
    bool CaptureFrame();  // Called by JNI thread

private:
    bool ProcessFrame(IDXGIResource* desktopResource);
    
    // D3D11/DXGI objects
    ComPtr<ID3D11Device> d3dDevice;
    ComPtr<ID3D11DeviceContext> d3dContext;
    ComPtr<IDXGIDevice> dxgiDevice;
    ComPtr<IDXGIAdapter> dxgiAdapter;
    ComPtr<IDXGIOutput> dxgiOutput;
    ComPtr<IDXGIOutput1> dxgiOutput1;
    ComPtr<IDXGIOutputDuplication> desktopDuplication;
    
    // Frame buffer
    ComPtr<ID3D11Texture2D> stagingTexture;
    D3D11_TEXTURE2D_DESC stagingDesc;
    
    // Capture region
    int captureX;
    int captureY;
    int captureWidth;
    int captureHeight;
    
    // Frame queue
    std::queue<FrameData> frameQueue;
    std::mutex queueMutex;
    std::atomic<bool> isCapturing;
    
    // FPS calculation
    std::chrono::high_resolution_clock::time_point lastFrameTime;
    std::atomic<double> currentFPS;
    int frameCount;
    
    static const int MAX_QUEUE_SIZE = 3; // Triple buffering
};

} // namespace fastrobot
