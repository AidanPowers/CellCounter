import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

class Main {
  final static File folder = new File("Images");


  public static void main(String[] args) throws IOException {
    //listFilesForFolder(folder);
    System.out.println("Main");
    //https://stackoverflow.com/questions/30486404/java-list-files-in-a-folder-avoiding-ds-store\
    File[] names = folder.listFiles((dir, name) -> !name.equals(".DS_Store"));
    System.out.println(names.length);

    ImageTester[] inputs= new ImageTester[names.length-15];
    int[] values= new int[inputs.length];

    for ( int i = 0; i< inputs.length;i++){
      inputs[i] = new ImageTester(names[i].toString());
      values[i] = inputs[i].countCell(15,i);
      //System.out.println(values[i]);
      inputs[i]=null;
    //trial3.writeOrigImage(1);
    }
    PrintWriter pw = new PrintWriter(new File("ImageData.csv"));
    StringBuilder sb = new StringBuilder();
    for (int i=0;i<values.length;i++){
      int fileRemove= folder.toString().length()+1;
      sb.append((names[i].toString()).substring(fileRemove)).append(",").append(values[i]).append("\r");
    }

    pw.write(sb.toString());
    pw.close();

  }
  //modified from
  //https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
  public static void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        listFilesForFolder(fileEntry);
      } else {
        System.out.println(fileEntry.getName());
      }
    }
  }


      /*ImageTester trial = new ImageTester("signal-2019-02-03-173726-2.jpg");
    trial.find();
    trial.toReadable();
    trial.writeImage(1);
    trial.round(3);
    trial.toReadable();
    trial.writeImage(2);
    trial.round(5);
    trial.toReadable();
    trial.writeImage(3);
    trial.reset();
    trial.writeImage(7);
    trial.roundedFind();
    trial.toReadable();
    trial.writeImage(4);
    ImageTester trial2 = new ImageTester("signal-2019-02-03-173726-2.jpg");
    trial2.find();
    trial2.round(8);
    trial2.reset();
    trial2.roundedFind();
    trial2.toReadable();
    trial2.writeImage(14);
        ImageTester trial2 = new ImageTester("signal-2019-02-03-173726-2.jpg");
    System.out.println(trial2.countCell(0,11));
    ImageTester trial1 = new ImageTester("signal-2019-02-03-173726-2.jpg");

    System.out.println(trial1.countCell(11,10));

    */
}