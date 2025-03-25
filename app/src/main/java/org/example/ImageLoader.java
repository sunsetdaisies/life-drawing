package org.example;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import java.io.InputStream;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ImageLoader {

  private ArrayList<Path> images;
  private ArrayList<Path> unusedImages;
  private Stack<Path> usedImages;
  private Path currentImage;
  private Path nextImage;


  public ImageLoader() {

    images = makeImageList((App.resourceDirectory != null) ? App.resourceDirectory : new File("resources"));
    unusedImages = new ArrayList<Path>(images);
    usedImages = new Stack<Path>();
  }

  public ArrayList<Path> makeImageList(File directory)  {
    File[] contents = directory.listFiles();

    ArrayList<Path> images = new ArrayList<Path>();

    for ( File f : contents) {
      Path path = f.toPath();
      if(path != null && isImage(path)) {
        images.add(path);
      }
    }
    //System.out.println(images.size());
    return images;
  }

  private boolean isImage(Path path)  {

    try {
      String type = Files.probeContentType(path);
      
      if (type != null && type.split("/")[0].equals("image")) {
        String suffix = type.split("/")[1];
        if (suffix.equals("bmp") || suffix.equals("png") || suffix.equals("jpeg") || suffix.equals("gif")) {
          return true;
        }
      }
    }
    catch(IOException e) { System.err.println("Somthing went very wrong");}
    return false;
  }

  public Image getImage() throws FileNotFoundException {
    
    if (nextImage == null) {
      nextImage = getPath();
    }

    usedImages.push(currentImage);
    currentImage = nextImage;
    nextImage = getPath();

    //System.out.println("left" + unusedImages.size());

    //return readImage(unusedImages.get(n));
    return readImage(currentImage);
  }

  private Path getPath() {
    Random rand = new Random();

    //System.out.println(unusedImages.size() == 0);

    if (unusedImages.size() == 0) {
      unusedImages = new ArrayList<Path>(images);
      //System.out.println("refreshed" + unusedImages.size());
    }

    int n = rand.nextInt(unusedImages.size());
    Path path = unusedImages.get(n);

    unusedImages.remove(n);

    return path;
  }

  public Image previousImage() throws FileNotFoundException, EmptyStackException {
    //System.out.println("used" + usedImages.size());
    nextImage = currentImage;
    currentImage = usedImages.pop();

    return readImage(currentImage);
  }

  private Image readImage(Path path) throws FileNotFoundException {
    File f = path.toFile();
    
    InputStream stream = new FileInputStream(f);
    Image image = new Image(stream);


    if (isRotated(f)) {
      ImageView iv = new ImageView(image);
      iv.setRotate(90);
      image = iv.snapshot(new SnapshotParameters(), null);
    }

    //ImageRotation imageRotation = new ImageRotation(image, isRotated(f));



    try {
      stream.close();
    }
    catch(IOException e){}

    return image;
  }

  private boolean isRotated(File f) {
    try {

      final ImageMetadata metadata = Imaging.getMetadata(f);
      //System.out.println(metadata);

      if (metadata instanceof JpegImageMetadata) { 
        JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        printTagValue(jpegMetadata, TiffTagConstants.TIFF_TAG_ORIENTATION);
        final TiffField field = jpegMetadata.findExifValueWithExactMatch(TiffTagConstants.TIFF_TAG_ORIENTATION);

        return (field != null && Short.parseShort(field.getValueDescription()) == 6);
        /*
        if(Short.parseShort(field.getValueDescription()) != 1) {
          System.out.println("it's a 6");
          
          TiffImageMetadata exif = jpegMetadata.getExif();
          TiffOutputSet outputSet = null;
          if (null != exif) {
            outputSet = exif.getOutputSet();
          }
          else {
            outputSet = new TiffOutputSet();
          }
          final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
          exifDirectory.removeField(TiffTagConstants.TIFF_TAG_ORIENTATION);
          exifDirectory.add(TiffTagConstants.TIFF_TAG_ORIENTATION, (short) 1);

          new ExifRewriter().updateExifMetadataLossless(f, os, outputSet);

          return os;
          
        }
        */
      }
      
    }
    catch (IOException e) {
      System.err.println("something went wrong");
    }
    return false;
  }

  private static void printTagValue(final JpegImageMetadata jpegMetadata, TagInfo tagInfo) {
    final TiffField field = jpegMetadata.findExifValueWithExactMatch(tagInfo);
    if (field == null) {
        System.out.println(tagInfo.name + ": " + "Not Found.");
    } else {
        System.out.println(tagInfo.name + ": " + field.getValueDescription());
    }
  }


} 

/*
class ImageRotation {
  Image image;
  boolean rotated;
  
  ImageRotation(Image image, boolean rotated) {
    this.image = image;
    //rotation = (rotated) ? 90 : 0;
    this.rotated = rotated;
  }

}
*/