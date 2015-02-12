package printerZebra;

import java.util.ArrayList;

/**
 * Created by JULIANEDUARDO on 12/02/2015.
 */
public class ClassPrinter {
    //private ClassFonts  localFonts;
    private ArrayList<ClassFonts> fonts = new ArrayList<ClassFonts>();
    private int     widthPrinter;
    private int     widthLabel;
    private int     marginTop;
    private int     marginLeft;
    private int     marginRight;
    private int     marginBotton;
    private int     currentLine;
    private int     finalLine;

    private boolean copyInformation;
    private String  strInformation;
    private String  strFile;


    public ClassPrinter(ClassPrinterBuilder builder){
        this.widthPrinter   = builder.widthPrinter;
    }


    public int getWidthPrinter() {
        return widthPrinter;
    }

    public int getWithLabel() {
        return widthLabel;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public int getMarginBotton() {
        return marginBotton;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public int getFinalLine() {
        return finalLine;
    }

    public String getStrInformation() {
        return strInformation;
    }

    public boolean isCopyInformation() {
        return copyInformation;
    }

    public String getStrFile() {
        return strFile;
    }


    public static class ClassPrinterBuilder{
        private int     widthPrinter;
        private int     widthLabel;
        private int     marginTop;
        private int     marginLeft;
        private int     marginRight;
        private int     marginBotton;
        private int     currentLine;
        private int     finalLine;

        public ClassPrinterBuilder(){

        }

        /*public ClassPrinterBuilder setWidthPrinter(int widthPrinter) {
            this.widthPrinter = widthPrinter;
        }

        public ClassPrinterBuilder setWidthLabel(int widthLabel) {
            this.widthLabel = widthLabel;
        }

        public ClassPrinterBuilder setMarginTop(int marginTop) {
            this.marginTop = marginTop;
        }

        public ClassPrinterBuilder setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;
        }

        public ClassPrinterBuilder setMarginRight(int marginRight) {
            this.marginRight = marginRight;
        }

        public ClassPrinterBuilder setMarginBotton(int marginBotton) {
            this.marginBotton = marginBotton;
        }

        public ClassPrinterBuilder setCurrentLine(int currentLine) {
            this.currentLine = currentLine;
        }

        public ClassPrinterBuilder setFinalLine(int finalLine) {
            this.finalLine = finalLine;
        }*/
    }
}
