package printerZebra;

import java.util.ArrayList;

/**
 * Created by JULIANEDUARDO on 12/02/2015.
 */
public class ClassPrinter {
    private ArrayList<ClassFonts>   lstFonts    = new ArrayList<ClassFonts>();
    private ClassFonts  font1;
    private ClassFonts  font2;
    private int         widthPrinter;
    private int         widthLabel;
    private int         marginTop;
    private int         marginLeft;
    private int         marginRight;
    private int         marginBotton;
    private int         currentLine;
    private int         finalLine;
    private int         spaceCharacter;

    private boolean copyInformation;
    private String  strInformation;
    private String  strFile;


    public ClassPrinter(ClassPrinterBuilder builder){
    }


    public void DrawImage(String NameFile, double PosX, double PosY){
        this.strInformation 	+= "PCX "+(this.marginLeft+PosX)+" "+(this.currentLine+PosY)+" !<"+NameFile+"\r\n";
        if(this.copyInformation){
            this.strFile+= "5;"+(this.marginLeft+PosX)+";"+(this.currentLine+PosY)+";"+NameFile+";\r\n";
        }
    }


    public void WrTitulo(String TextoTitulo, double SaltoLineaPre, double SaltoLineaPos){
        double Char2Line;
        String Words[];
        String WordsLine;
        double Justificacion;

        this.font1   = this.getDataFont("TITULO");
        WordsLine = "";
        Justificacion = 0;

        this.currentLine += this.font1.getHeight_font() * SaltoLineaPre;
        Char2Line = this.widthLabel / this.font1.getWidth_font();
        Words = TextoTitulo.split(" ");

        for(int i = 0; i<Words.length;i++){
            WordsLine += Words[i] + " ";
            if (i < Words.length){
                Justificacion = Words[i].length();
            }else{
                Justificacion = 0;
            }

            if ((WordsLine.length() + Justificacion) > Char2Line){
                Justificacion = (this.widthLabel - (WordsLine.length() * this.font1.getWidth_font())) / 2;
                this.strInformation += "TEXT " + this.font1.getName_font() + " 0 " + Justificacion + " " + this.currentLine + " " + WordsLine +"\r\n";
                if(this.copyInformation){
                    this.strFile += "1;"+Justificacion+";"+this.currentLine+";"+WordsLine+";\r\n";
                }
                this.currentLine += this.font1.getHeight_font();
                WordsLine = "";
            }
        }
        Justificacion = (this.widthLabel - (WordsLine.length() * this.font1.getWidth_font())) / 2;
        this.strInformation += "TEXT " + this.font1.getName_font()+ " 0 " + Justificacion + " " + this.currentLine + " " + WordsLine + "\r\n";
        if(this.copyInformation){
            this.strInformation += "1;"+Justificacion+";"+this.currentLine+";"+WordsLine+";\r\n";
        }
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPos;
    }


    public void WrLabel(String Etiqueta, String InfEtiqueta, int OffsetWrLabel, double SaltoLineaPre, double SaltoLineaPos){
        this.font1   = this.getDataFont("LABEL");
        this.font1   = this.getDataFont("REGULAR");
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPre;
        this.strInformation += "TEXT " + this.font1.getName_font() + " 0 " + OffsetWrLabel + " " + this.currentLine + " " + Etiqueta + " \r\n";
        this.strInformation += "TEXT " + this.font2.getName_font() + " 0 " + (OffsetWrLabel + ((Etiqueta.length() + 1) * this.font1.getWidth_font())) + " " + this.currentLine+ " " + InfEtiqueta + "\r\n";

        if(this.copyInformation){
            this.strFile += "3;"+OffsetWrLabel+";"+ this.currentLine+";"+Etiqueta+";"+InfEtiqueta+";\r\n";
        }
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPos;
    }


    public void JustInformation(String Informacion, int OffsetInformation, double SaltoLineaPre, double SaltoLineaPos){
        this.font1   = this.getDataFont("REGULAR");
        double Char2Line = (this.widthLabel - OffsetInformation) / this.font1.getWidth_font();
        this.currentLine += this.font1.getHeight_font()* SaltoLineaPre;

        while (Informacion.length() > Char2Line){
            this.strInformation += "TEXT " + this.font1.getName_font() + " 0 " + OffsetInformation + " " + this.currentLine+ " " + Informacion.substring(0, (int)Char2Line) + " \r\n";
            if(this.copyInformation){
                this.strFile += "4;"+OffsetInformation+";"+this.currentLine+";"+Informacion.substring(0, (int)Char2Line)+";\r\n";
            }
            Informacion = Informacion.substring((int)Char2Line);
            this.currentLine += this.font1.getHeight_font();
        }
        this.strInformation += "TEXT " + this.font1.getName_font()+ " 0 " + OffsetInformation + " " + this.currentLine+ " " + Informacion + " \r\n";
        if(this.copyInformation){
            this.strFile += "4;"+OffsetInformation+";"+this.currentLine+";"+Informacion+";\r\n";
        }
        this.currentLine += this.font1.getHeight_font()* SaltoLineaPos;
    }


    public void WrSubTitulo(String InfSubtitulo, int OffsetSub, double SaltoLineaPre, double SaltoLineaPos){
        this.font1 = this.getDataFont("SUBTITULO");
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPre;

        this.strInformation += "TEXT " + this.font1.getName_font()+ " 0 " + OffsetSub + " " + this.currentLine+ " " + InfSubtitulo + " \r\n";
        if(this.copyInformation){
            this.strFile += "2;"+OffsetSub+";"+this.currentLine+";"+InfSubtitulo+";\r\n";
        }
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPos;
    }


    public void WrRectangle(double PosX1, double PosY1, double PosX2, double PosY2, double Incremento, int Shadow){
        double IncLine = 0;
        int i;
        this.strInformation += "BOX " + PosX1 + " " + PosY1 + " " + PosX2 + " " + PosY2 + " 2 \r\n";

        if (Shadow != 0){
            IncLine = (PosX2 - PosX1) / Shadow;
            for (i = 0; i<Shadow;i++){
                this.strInformation += "BOX " + (PosX1 + (IncLine * i)) + " " + PosY1 + " " + (PosX1 + (IncLine * i)) + " " + PosY2 + " 0 \r\n";
            }
            for(i = 0;i<Shadow;i++){
                this.strInformation += "BOX " + PosX1 + " " + (PosY1 + (IncLine * i)) + " " + PosX2 + " " + (PosY1 + (IncLine * i)) + " 0 \r\n";
            }
        }

        if (Incremento != 0) {
            this.currentLine += PosY2 - PosY1;
        }
    }


    public String getDoLabel(){
        this.strInformation = "! " + this.marginLeft+ " 200 200 " + (this.currentLine + this.finalLine) + " 1" + " \r\n" + "ENCODING UTF-8 \r\n LABEL " + " \r\n" + this.strInformation+ " \r\n";
        this.strInformation += "PRINT \r\n";
        return this.strInformation;
    }


    public void resetEtiqueta(){
        this.strInformation 	= "";
        this.strFile 	        = "";
        this.currentLine 	    = this.marginTop;
        this.finalLine          = this.marginBotton;
    }


    private ClassFonts getDataFont(String _descriptionFont){
        ClassFonts  myFont  = new ClassFonts();
        for(int i = 0;i<this.lstFonts.size();i++){
            if(this.lstFonts.get(i).getDescription_font().equals(_descriptionFont)){
                myFont  =   this.lstFonts.get(i);
            }
        }
        return myFont;
    }


    public ArrayList<ClassFonts> getLstFonts() {
        return lstFonts;
    }

    public int getWidthPrinter() {
        return widthPrinter;
    }

    public int getWidthLabel() {
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

    public int getSpaceCharacter() {
        return spaceCharacter;
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
        private ClassFonts              font        = new ClassFonts();
        private ArrayList<ClassFonts>   lstFonts    = new ArrayList<ClassFonts>();
        private int     widthPrinter;
        private int     widthLabel;
        private int     marginTop;
        private int     marginLeft;
        private int     marginRight;
        private int     marginBotton;
        private int     currentLine;
        private int     finalLine;
        private int     spaceCharacter;

        public ClassPrinterBuilder(){

        }

        public ClassPrinterBuilder addFonts(String _descriptionFont, String _nameFont, int _width, int _height){
            this.font.setDescription_font(_descriptionFont.toUpperCase());
            this.font.setName_font(_nameFont.toUpperCase());
            this.font.setWidth_font(_width);
            this.font.setHeight_font(_height);
            this.lstFonts.add(this.font);
            return this;
        }

        public ClassPrinterBuilder setWidthPrinter(int widthPrinter) {
            this.widthPrinter = widthPrinter;
            return this;
        }

        public ClassPrinterBuilder setWidthLabel(int widthLabel) {
            this.widthLabel = widthLabel;
            return this;
        }

        public ClassPrinterBuilder setMarginTop(int marginTop) {
            this.marginTop = marginTop;
            return this;
        }

        public ClassPrinterBuilder setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;
            return this;
        }

        public ClassPrinterBuilder setMarginRight(int marginRight) {
            this.marginRight = marginRight;
            return this;
        }

        public ClassPrinterBuilder setMarginBotton(int marginBotton) {
            this.marginBotton = marginBotton;
            return this;
        }

        public ClassPrinterBuilder setCurrentLine(int currentLine) {
            this.currentLine = currentLine;
            return this;
        }

        public ClassPrinterBuilder setFinalLine(int finalLine) {
            this.finalLine = finalLine;
            return this;
        }

        public ClassPrinterBuilder setSpaceCharacter(int spaceCharacter) {
            this.spaceCharacter = spaceCharacter;
            return this;
        }

        public ClassPrinter build(){
            return new ClassPrinter(this);
        }
    }
}
