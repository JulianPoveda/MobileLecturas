package printerZebra;

import java.util.ArrayList;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

/**
 * Created by JULIANEDUARDO on 12/02/2015.
 */
public class ClassPrinter {
    private Connection printerConnection;
    private ZebraPrinter printer;

    private ArrayList<ClassFonts>   lstFonts    = new ArrayList<ClassFonts>();
    private ClassFonts  font;
    //private ClassFonts  font2;
    private boolean     verticalPrinter;
    private boolean     copyInformation;

    private int         sizePageX;
    private int         sizePageY;

    private int         marginTop;
    private int         marginLeft;
    private int         marginRight;
    private int         marginBotton;

    private int         sizeLabelX;
    private int         sizeLabelY;

    private int         currentPointX;
    private int         currentPointY;

    /*private int         widthPrinter;
    private int         widthLabel;


    private int         spaceCharacter;

    private int         finalLine;*/


    private String      strInformation;
    private String      strFile;


    public ClassPrinter(boolean _copiaImpresion){
        this.copyInformation    = _copiaImpresion;
        this.strInformation     = "";
        this.strFile            = "";
    }


    public void SetSizePage(int _sizeX, int _sizeY){
        this.sizePageX  = _sizeX;
        this.sizePageY  = _sizeY;
    }

    public void SetSizeMargin(int _marginTop, int _marginLeft, int _marginRight, int _marginBotton){
        this.marginTop      = _marginTop;
        this.marginLeft     = _marginLeft;
        this.marginRight    = _marginRight;
        this.marginBotton   = _marginBotton;

        this.sizeLabelX     = this.sizePageX - this.marginLeft - this.marginRight;
        this.sizeLabelY     = this.sizePageY - this.marginTop - this.marginBotton;

        if(this.verticalPrinter){
            this.currentPointY  = this.marginLeft;
            this.currentPointX  = this.sizePageY-this.marginTop;
        }else{
            this.currentPointY  = this.marginTop;
            this.currentPointX  = this.marginLeft;
        }
    }


    public void setVerticalPrinter(boolean _orientation){
        this.verticalPrinter = _orientation;
    }

    public boolean getVerticalPrinter(){
        return this.verticalPrinter;
    }

    public void setRegisterFont(String _nameFont, String _descriptionFont, int _sizeFont, int _widthFont, int _heightFont){
        this.font   = new ClassFonts();
        this.font.setName_font(_nameFont);
        this.font.setDescription_font(_descriptionFont);
        this.font.setWidth_font(_widthFont);
        this.font.setHeight_font(_heightFont);
        this.font.setSize_font(_sizeFont);
        this.lstFonts.add(this.font);
    }


    public void WriteDefaultText(String _typeText, int _posX, double _preIncremento, double _posIncremento, String _text){
        this.font = this.getDataFont(_typeText);
        if(this.verticalPrinter){
            this.currentPointX -= _preIncremento*this.font.getHeight_font();
            this.strInformation += "TEXT270 "+this.font.getName_font()+" "+this.font.getSize_font()+ " "+(this.currentPointX)+" "+(this.currentPointY+_posX)+" "+_text+"\r\n";
            this.currentPointX -= _posIncremento*this.font.getHeight_font();
        }else{
            this.currentPointY += _preIncremento*this.font.getHeight_font();
            this.strInformation += "TEXT "+this.font.getName_font()+" "+this.font.getSize_font()+ " "+(this.currentPointX+_posX)+" "+(this.currentPointY)+" "+_text+"\r\n";
            this.currentPointY += _posIncremento*this.font.getHeight_font();
        }
    }

    /*public void WriteScaleText(String _typeText, int _posX, int _posY, int _preIncremento, int _posIncremento, String _text){
        this.font = this.getDataFont(_typeText);
        this.currentLine    += _preIncremento*this.font.getHeight_font();
        if(this.verticalPrinter){
            this.strInformation += "VSCALE-TEXT "+this.font.getName_font()+" "+this.font.getWidth_font()+" "+this.font.getHeight_font()+ " "+_posX+" "+(this.currentLine+_posY)+" "+_text+"\r\n";
        }else{
            this.strInformation += "SCALE-TEXT "+this.font.getName_font()+" "+this.font.getWidth_font()+" "+this.font.getHeight_font()+ " "+_posX+" "+(this.currentLine+_posY)+" "+_text+"\r\n";
        }
        this.currentLine    += _posIncremento*this.font.getHeight_font();
    }*/


    public ClassFonts getDataFont(String _descripcionFont){
        ClassFonts localFont = new ClassFonts();
        for(int i=0;i<this.lstFonts.size();i++){
            if(this.lstFonts.get(i).getDescription_font().equals(_descripcionFont)){
                localFont   = this.lstFonts.get(i);
            }
        }
        return localFont;
    }



    /*public void DrawImage(String NameFile, double PosX, double PosY){
        this.strInformation 	+= "PCX "+(this.marginLeft+PosX)+" "+(this.currentLine+PosY)+" !<"+NameFile+"\r\n";
        if(this.copyInformation){
            this.strFile+= "5;"+(this.marginLeft+PosX)+";"+(this.currentLine+PosY)+";"+NameFile+";\r\n";
        }
    }*/


    /*public void WrTitulo(String TextoTitulo, double SaltoLineaPre, double SaltoLineaPos){
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
    }*/


    /*public void WrLabel(String Etiqueta, String InfEtiqueta, int OffsetWrLabel, double SaltoLineaPre, double SaltoLineaPos){
        this.font1   = this.getDataFont("LABEL");
        this.font1   = this.getDataFont("REGULAR");
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPre;
        this.strInformation += "TEXT " + this.font1.getName_font() + " 0 " + OffsetWrLabel + " " + this.currentLine + " " + Etiqueta + " \r\n";
        this.strInformation += "TEXT " + this.font2.getName_font() + " 0 " + (OffsetWrLabel + ((Etiqueta.length() + 1) * this.font1.getWidth_font())) + " " + this.currentLine+ " " + InfEtiqueta + "\r\n";

        if(this.copyInformation){
            this.strFile += "3;"+OffsetWrLabel+";"+ this.currentLine+";"+Etiqueta+";"+InfEtiqueta+";\r\n";
        }
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPos;
    }*/


    /*public void JustInformation(String Informacion, int OffsetInformation, double SaltoLineaPre, double SaltoLineaPos){
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
    }*/


    /*public void WrSubTitulo(String InfSubtitulo, int OffsetSub, double SaltoLineaPre, double SaltoLineaPos){
        this.font1 = this.getDataFont("SUBTITULO");
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPre;

        this.strInformation += "TEXT " + this.font1.getName_font()+ " 0 " + OffsetSub + " " + this.currentLine+ " " + InfSubtitulo + " \r\n";
        if(this.copyInformation){
            this.strFile += "2;"+OffsetSub+";"+this.currentLine+";"+InfSubtitulo+";\r\n";
        }
        this.currentLine += this.font1.getHeight_font() * SaltoLineaPos;
    }*/


    /*public void WrRectangle(double PosX1, double PosY1, double PosX2, double PosY2, double Incremento, int Shadow){
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
    }*/

    public String getDoLabel(){
        if(verticalPrinter){
            this.strInformation = "! 0 200 200 " + (this.sizePageX) + " 1" + " \r\n" + "ENCODING UTF-8 \r\n LABEL " + " \r\n" + this.strInformation;
        }else{
            this.strInformation = "! 0 200 200 " + (this.sizePageY) + " 1" + " \r\n" + "ENCODING UTF-8 \r\n LABEL " + " \r\n" + this.strInformation;
        }
        this.strInformation += "PRINT \r\n";
        return this.strInformation;
    }

    public void printLabel(String _bluetooth) {
        printer = this.Zebra_Connect(_bluetooth);
        if (printer != null) {
            try {
                byte[] configLabel = this.convertExtendedAscii(this.getDoLabel());
                //this.getDoLabel().getBytes();
                printerConnection.write(configLabel);
                //setStatus("Sending Data", Color.BLUE);
                DemoSleeper.sleep(150);
                if (printerConnection instanceof BluetoothConnection) {
                    //String friendlyName = ((BluetoothConnection) printerConnection).getFriendlyName();
                    //setStatus(friendlyName, Color.MAGENTA);
                    //DemoSleeper.sleep(500);
                }
            } catch (ConnectionException e) {
                //setStatus(e.getMessage(), Color.RED);
            } finally {
                this.Zebra_Disconnect();
            }
        } else {
            this.Zebra_Disconnect();
        }
    }

    private ZebraPrinter Zebra_Connect(String _bluetooth) {
        ZebraPrinter printer= null;
        printerConnection 	= new BluetoothConnection(_bluetooth);
        try {
            printerConnection.open();
        } catch (ConnectionException e) {
            DemoSleeper.sleep(100);
            this.Zebra_Disconnect();
        }

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
                //PrinterLanguage pl = printer.getPrinterControlLanguage();
            } catch (ConnectionException e) {
                printer = null;
                DemoSleeper.sleep(100);
                this.Zebra_Disconnect();
            } catch (ZebraPrinterLanguageUnknownException e) {
                printer = null;
                DemoSleeper.sleep(100);
                this.Zebra_Disconnect();
            }
        }
        return printer;
    }

    private void Zebra_Disconnect() {
        try {
            if (printerConnection != null) {
                printerConnection.close();
            }
        } catch (ConnectionException e) {
        } finally {
        }
    }

    private byte[] convertExtendedAscii(String input){
        int length = input.length();
        byte[] retVal = new byte[length];
        for(int i=0; i<length; i++){
            char c = input.charAt(i);
            if (c < 127){
                retVal[i] = (byte)c;
            }else{
                retVal[i] = (byte)(c - 256);
            }
        }
        return retVal;
    }

    public void resetEtiqueta(){
        this.strInformation 	= "";
        this.strFile 	        = "";
        if(this.verticalPrinter){
            this.currentPointY  = this.marginLeft;
            this.currentPointX  = this.sizePageY-this.marginTop;
        }else{
            this.currentPointY  = this.marginTop;
            this.currentPointX  = this.marginLeft;
        }
    }



    public ArrayList<ClassFonts> getLstFonts() {
        return lstFonts;
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

    public int getCurrentPosX() {
        return this.currentPointX;
    }

    public int getCurrentPosY() {
        return this.currentPointY;
    }

    /*public int getFinalLine() {
        return finalLine;
    }*/

    /*public int getSpaceCharacter() {
        return spaceCharacter;
    }*/

    public String getStrInformation() {
        return strInformation;
    }

    public boolean isCopyInformation() {
        return copyInformation;
    }

    public String getStrFile() {
        return strFile;
    }



    /*public static class ClassPrinterBuilder{
        private ClassFonts              font        = new ClassFonts();
        private ArrayList<ClassFonts>   lstFonts    = new ArrayList<ClassFonts>();
        private int         widthPrinter;
        private int         widthLabel;
        private int         marginTop;
        private int         marginLeft;
        private int         marginRight;
        private int         marginBotton;
        private int         currentLine;
        private int         finalLine;
        private int         spaceCharacter;
        private boolean     copyInformation;

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

        public ClassPrinterBuilder setCopyInformation(boolean copyInformation) {
            this.copyInformation = copyInformation;
            return this;
        }

        public ClassPrinter build(){
            return new ClassPrinter(this);
        }
    }*/
}
