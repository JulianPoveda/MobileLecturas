package printerZebra;

/**
 * Created by JULIANEDUARDO on 12/02/2015.
 */
public class ClassFonts {
    protected String    description_font;
    protected String    name_font;
    protected int       width_font;
    protected int       height_font;
    protected int       size_font;

    public ClassFonts(){
        //empty contructor;
    };

    public String getDescription_font() {
        return description_font;
    }

    public void setDescription_font(String description_font) {
        this.description_font = description_font;
    }

    public int getWidth_font() {
        return width_font;
    }

    public void setWidth_font(int width_font) {
        this.width_font = width_font;
    }

    public int getHeight_font() {
        return height_font;
    }

    public void setHeight_font(int height_font) {
        this.height_font = height_font;
    }

    public String getName_font() {
        return name_font;
    }

    public void setName_font(String name_font) {
        this.name_font = name_font;
    }

    public int getSize_font() {
        return size_font;
    }

    public void setSize_font(int size_font) {
        this.size_font = size_font;
    }
}
