import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

class ImageTester {
    private int[][] pic;
    private boolean[][] isScannned;

    public ImageTester() throws IOException {
    System.out.println("Image Tester");
    //test
    BufferedImage img = ImageIO.read(new File("signal-2019-02-03-173726-2.jpg"));
    pic = new int[img.getWidth()][img.getHeight()];
    isScannned = new boolean[img.getWidth()][img.getHeight()];

      PrintWriter pw = new PrintWriter(new File("Output.csv"));
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i<img.getHeight();i = i + 1){
          for (int j = 0; j < img.getWidth(); j = j + 1){
              pic[j][i] = img.getRGB(j,i)& 0xFF;
              sb.append(img.getRGB(j,i)& 0xFF);
              sb.append(",");

          }
          sb.append("\r");
      }
      pw.write(sb.toString());
      pw.close();
      System.out.println("Fin");

  }
  public boolean sweep(int x,int y){
      System.out.println(x + " "+ y);
        if(x>=pic[0].length || y>=pic.length||y<0||x<0) {
          return false;
      }

        if (isScannned[y][x]) {
            return false;
        }
        if (pic[y][x] > 1) {
            return false;
        }
        isScannned[y][x] = true;

        sweep(x+1, y+1);
        sweep(x+1, y-1);
        sweep(x+1, y);
        sweep(x, y+1);
        sweep(x, y-1);
        sweep(x-1, y+1);
        sweep(x-1, y-1);
        sweep(x-1, y);
        return false;

  }
  public void find(){
      for (int i = 0; i<pic.length;i = i + 1){
          for (int j = 0; j < pic[i].length-2000; j = j + 1){
            if (!isScannned[i][j]){
                sweep(i,j);
            }
          }
      }
  }
  public void toReadable() throws FileNotFoundException
    {

        PrintWriter pw = new PrintWriter(new File("Output2.csv"));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<pic.length;i = i + 1){
            for (int j = 0; j < pic[i].length-2000; j = j + 1){
                if (isScannned[i][j]){

                    sb.append("1");
                }
                else {
                    sb.append("0");
                }

            }
            sb.append("\r");

        }
        pw.write(sb.toString());
        pw.close();
    }
}