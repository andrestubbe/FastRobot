import fastrobot.FastRobot;
import org.openjdk.jmh.annotations.*;

import java.awt.Robot;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class Benchmark {

    private FastRobot fastRobot;
    private Robot awtRobot;

    @Setup
    public void setup() throws Exception {
        fastRobot = new FastRobot();
        awtRobot = new Robot();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public int benchmarkFastRobotGetPixelColor() {
        return fastRobot.getPixelColor(100, 100);
    }

    @org.openjdk.jmh.annotations.Benchmark
    public int benchmarkAwtRobotGetPixelColor() {
        return awtRobot.getPixelColor(100, 100).getRGB();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public int[] benchmarkFastRobotGetMousePosition() {
        return fastRobot.getMousePosition();
    }

    @org.openjdk.jmh.annotations.Benchmark
    public int benchmarkFastRobotScreenDimensions() {
        return fastRobot.getScreenWidth() + fastRobot.getScreenHeight();
    }
}
