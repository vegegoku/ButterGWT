package org.akab.buttergwt.wizard;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility
{
  private static final int BUFFER_SIZE = 4096;
  
  public static void unzip(InputStream zipFileInputStream, String destDirectory)
    throws IOException
  {
    File destDir = new File(destDirectory);
    if (!destDir.exists()) {
      destDir.mkdir();
    }
    ZipInputStream zipIn = new ZipInputStream(zipFileInputStream);
    
    ZipEntry entry = zipIn.getNextEntry();
    while (entry != null)
    {
      String filePath = destDirectory + File.separator + entry.getName();
      if (!entry.isDirectory())
      {
        extractFile(zipIn, filePath);
      }
      else
      {
        File dir = new File(filePath);
        dir.mkdir();
      }
      zipIn.closeEntry();
      entry = zipIn.getNextEntry();
    }
    zipIn.close();
  }
  
  private static void extractFile(ZipInputStream zipIn, String filePath)
    throws IOException
  {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte['က'];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }
}
