package net.sourceforge.pinemup.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

import net.sourceforge.pinemup.core.Category;
import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.Note;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExportFileManager {
   private static final String EXPORT_FILE_ENCODING = "UTF-8";
   private static final String FILE_EXTENSION = ".txt";
   private static final String NOTE_SEPARATOR = "---------------------";
   private static final String CATEGORY_SEPARATOR = "################################################################";

   private static final Logger LOG = LoggerFactory.getLogger(ExportFileManager.class);

   private static class Holder {
      private static final ExportFileManager INSTANCE = new ExportFileManager();
   }

   public static ExportFileManager getInstance() {
      return Holder.INSTANCE;
   }

   private ExportFileManager() {

   }

   public void exportCategoriesToTextFile(List<Category> l, String fileName) {
      String checkedFileName = NotesFileManager.checkAndAddExtension(fileName, FILE_EXTENSION);
      File f = new File(checkedFileName);
      if (f != null) {
         try {
            PrintWriter ostream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), Charset.forName(EXPORT_FILE_ENCODING)
                  .newEncoder()));

            for (Category cat : l) {
               ostream.println(CATEGORY_SEPARATOR);
               ostream.println(I18N.getInstance().getString("category") + ": " + cat.getName());
               ostream.println(CATEGORY_SEPARATOR);
               ostream.println();

               boolean firstNote = true;
               for (Note n : cat.getNotes()) {
                  if (n.getText() != null && !n.getText().trim().equals("")) {
                     if (!firstNote) {
                        ostream.println();
                        ostream.println(NOTE_SEPARATOR);
                     } else {
                        firstNote = false;
                     }
                     ostream.println(n.getText().replaceAll("\n", "\r\n"));
                     ostream.println();
                  }
               }
            }
            ostream.flush();
            ostream.close();
         } catch (IOException e) {
            LOG.error("Error during attempt to export notes.", e);
         }
      }
   }
}
