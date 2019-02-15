import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

class ImageTester {
    private int[][] pic;
    private int[][] debug;
    private int[][] outputImage;
    private boolean[][] goodCell;
    private boolean[][] isScannned;
    private boolean[][] scannable;
    private int debugTally= 0;
    private int layer = 0;
    private int cells=0;
    private int fakes = 0;
    private boolean fake = false;
    private int threshHold = 150;
    private int cycle;
    private String currentImage;

    public ImageTester(String image) throws IOException {
        System.out.println("Image Tester");
//test
        currentImage = image;
        BufferedImage img = ImageIO.read(new File(image));
        pic = new int[img.getWidth()][img.getHeight()];
        debug = new int[img.getWidth()][img.getHeight()];
        outputImage = new int[img.getWidth()][img.getHeight()];
        goodCell = new boolean[img.getWidth()][img.getHeight()];
        isScannned = new boolean[img.getWidth()][img.getHeight()];
        scannable = new boolean[img.getWidth()][img.getHeight()];

        //PrintWriter pw = new PrintWriter(new File("Output.csv"));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<img.getHeight();i = i + 1){
            for (int j = 0; j < img.getWidth(); j = j + 1){

                pic[j][i] = img.getRGB(j,i)& 0xFF;

                if (pic[j][i] < threshHold){
                    scannable[j][i] = true;
                }
                sb.append(img.getRGB(j,i)& 0xFF);
                sb.append(",");

            }
            sb.append("\r");
        }
        //pw.write(sb.toString());
        //pw.close();
        System.out.println("Fin");

    }
    private void targetSweep(int x, int y, int goal,int replace){
        if(x>=pic[0].length || y>=pic.length||y<0||x<0) {
            return;
        }
        if (debug[y][x]!=goal){
            return;
        }
        debug[y][x]=replace;
        targetSweep(x+1, y+1,goal,replace);
        targetSweep(x+1, y-1,goal,replace);
        targetSweep(x+1, y,goal,replace);
        targetSweep(x, y+1,goal,replace);
        targetSweep(x, y-1,goal,replace);
        targetSweep(x-1, y+1,goal,replace);
        targetSweep(x-1, y-1,goal,replace);
        targetSweep(x-1, y,goal,replace);

    }
    public int sweep(int y,int x,int lastX, int lastY){

        //System.out.println(x + " "+ y + " " + layer);
        if(x>=pic[0].length || y>=pic.length||y<0||x<0) {
            layer = layer - 1;
            return layer;
        }
        boolean wasScaned = isScannned[y][x];
        //isScannned[y][x] = true;
        if(wasScaned||!scannable[y][x]){
            //pulseStarter(x,y,0);

            return layer;
        }
        layer++;
        if (layer>2000)
        {
            fake = true;
            return layer;

        }


        if (wasScaned) {
            layer = layer - 1;
            //System.out.println("test");

            return layer;
        }
        if (pic[y][x] > threshHold)//Math.abs(pic[y][x] - pic[lastY][lastX]) > 1 )
        {
            layer = layer - 1;

            return layer;
        }
        isScannned[y][x] = true;

        sweep(x+1, y+1,x,y);
        sweep(x+1, y-1,x,y);
        sweep(x+1, y,x,y);
        sweep(x, y+1,x,y);
        sweep(x, y-1,x,y);
        sweep(x-1, y+1,x,y);
        sweep(x-1, y-1,x,y);
        sweep(x-1, y,x,y);
        //layer = layer - 1;

        return layer;

    }
    private void pulseStarter(int x, int y, int i){
        pulse(x+1, y+1,i++);
        pulse(x+1, y-1,i++);
        pulse(x+1, y,i++);
        pulse(x, y+1,i++);
        pulse(x, y-1,i++);
        pulse(x-1, y+1,i++);
        pulse(x-1, y-1,i++);
        pulse(x-1, y,i++);
    }
    private void pulse(int x, int y, int i){
        layer++;
        if (layer>3000)
        {

            return ;

        }
        if(x>=pic[0].length || y>=pic.length||y<0||x<0) {
            return;
        }
        if (scannable[y][x]){
            int size = sweep(x, y, x, y);
            return;
        }
        if (i>3){
            return;
        }
        if (!isScannned[y][x]&&pic[y][x]<threshHold) {
            int size = sweep(x, y, x, y);
        }
        isScannned[y][x] = true;


        return;
    }
    public void find(){
        for (int i = 0; i<pic.length;i = i + 1){
            for (int j = 0; j < pic[i].length; j = j + 1){
                if (pic[i][j]<threshHold){
                    layer = 0;

                    int size = sweep(i,j,i,j);

                    if (size>20){
                        //System.out.println(i + " "+ j + " " + size);
                        cells++;}
                    if(fake){
                        fake = false;
                        fakes++;
                    }
                    else goodCell[i][j] = true;
                    layer = 0;
                    //System.out.println("workin");
                }
            }
        }
        //System.out.println(cells + " " + fakes + " " + (cells-fakes));
    }
    private void reset(){
        cells = 0;
        fakes=0;
        for (int i = 0; i<pic.length;i = i + 1){
            for (int j = 0; j < pic[i].length; j = j + 1){
                scannable[i][j]=isScannned[i][j];
                isScannned[i][j]=false;
            }
        }
    }
    public  void round(int num){
        for (int i = 1; i <= num ; i++){
            round();
        }
    }
    public void round(){
        boolean[][] wasScannned = new boolean[pic.length][pic[0].length];
        for (int i = 0; i<pic.length;i = i + 1){
            if (pic[i].length >= 0) {
                System.arraycopy(isScannned[i], 0, wasScannned[i], 0, pic[i].length);
            }
        }
        for (int i = 1; i<pic.length-1;i = i + 1){
            for (int j = 1; j < pic[i].length-1; j = j + 1){
                if(wasScannned[i][j]){

                    isScannned[i+1][j-1]=true;
                    isScannned[i+1][j+1]=true;
                    isScannned[i+1][j+0]=true;
                    isScannned[i+0][j+1]=true;
                    isScannned[i+0][j-1]=true;
                    isScannned[i-1][j-1]=true;
                    isScannned[i-1][j+1]=true;
                    isScannned[i-1][j+0]=true;
                }
            }
        }
    }
    public void roundedFind(){

        for (int i = 0; i<pic.length;i = i + 1) {
            for (int j = 0; j < pic[i].length; j = j + 1) {
                if(scannable[i][j]&&!isScannned[i][j]){
                    layer = 0;
                    cycle++;
                    loading(cycle);
                    try{
                        roundedSweep(j,i);

                    }
                    catch (StackOverflowError e){
                    }
                    if (layer>2000 && layer<10000){
                        targetSweep(j,i,cycle,1000);
                        cells++;
                    }
                    //System.out.println(cells+ " " + layer);

                    //int size = layer;

                    //if (size>20){
                     //   System.out.println(i + " "+ j + " " + size);
                     //   cells++;}
                    //if(fake){
                    //    fake = false;
                     //   fakes++;
                 //   }
                }
            }
        }

    }
    public void roundedSweep(int x, int y){
        layer++;
        if(x>=pic[0].length || y>=pic.length||y<0||x<0) {
            return;
        }
        if(!scannable[y][x]){
            return;
        }
        if(isScannned[y][x]){
            return;
        }
        debug[y][x]= cycle;
        isScannned[y][x]=true;
        roundedSweep(x+1, y+1);
        roundedSweep(x+1, y-1);
        roundedSweep(x+1, y);
        roundedSweep(x, y+1);
        roundedSweep(x, y-1);
        roundedSweep(x-1, y+1);
        roundedSweep(x-1, y-1);
        roundedSweep(x-1, y);
    }
    public void toReadable() throws FileNotFoundException
    {
        for (int j = 0; j < pic[0].length; j = j + 1){
            for (int i = 0; i<pic.length;i = i + 1){
                outputImage[i][j]= 0;
            }
        }

           // PrintWriter pw = new PrintWriter(new File("Output2.csv"));

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < pic[0].length; j = j + 1){

            for (int i = 0; i<pic.length;i = i + 1){
                int tempVal = 0;
                if (debug[i][j]== 1000){
                    tempVal=255;
                }
                //if (scannable[i][j]){
                //    tempVal=255;
                //    sb.append("1");
                //}
                //else {
                //    tempVal=0;
                //    sb.append("0");
                //}
                outputImage[i][j]= tempVal;
                sb.append(tempVal+",");


            }
            sb.append("\r");

        }
        //pw.write(sb.toString());
        //pw.close();
    }
    //https://stackoverflow.com/questions/10767471/convert-2d-array-in-java-to-image
    public void writeImage(int Name) {
        String path = "Outputs/Seen"+Name+".png";
        BufferedImage image = new BufferedImage(outputImage.length, outputImage[0].length, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < outputImage.length; x++) {
            for (int y = 0; y < outputImage[0].length; y++) {
                image.setRGB(x, y, outputImage[x][y]);
            }
        }

        File ImageFile = new File(path);

        try {
            ImageFile.createNewFile();
            ImageIO.write(image, "png", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeOrigImage(int Name) {
        String path = "Outputs/Orig"+Name+".png";
        BufferedImage image = new BufferedImage(pic.length, pic[0].length, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < pic.length; x++) {
            for (int y = 0; y < pic[0].length; y++) {
                //loading(x);
                image.setRGB(x, y, pic[x][y] * 0x00010101);

            }
        }

        File ImageFile = new File(path);

        try {
            ImageFile.createNewFile();
            ImageIO.write(image, "png", ImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int countCell(int round, int imageName){
        writeOrigImage(imageName);
        find();
        round(round);
        reset();
        roundedFind();
        try{
            toReadable();
        }
        catch ( FileNotFoundException e){}
        writeImage(imageName);
        return cells;
    }
    private void loading(int i){
        if (i % 4 ==0){
            System.out.print("|\r");
        }
        else if (i % 4 ==1){
            System.out.print("/\r");
        }
        else if (i % 4 ==2){
            System.out.print("-\r");
        }
        else if (i % 4 ==3){
            System.out.print("\\\r");
        }

    }
}