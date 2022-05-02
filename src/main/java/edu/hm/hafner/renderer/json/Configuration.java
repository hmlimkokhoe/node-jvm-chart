package edu.hm.hafner.renderer.json;

/**
 * Configures the ECharts output.
 */
public class Configuration {
    private int width;
    private int height;

    public Configuration(final int width, final int height) {
        super();

        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
