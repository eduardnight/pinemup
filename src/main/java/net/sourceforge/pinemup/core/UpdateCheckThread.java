/*
 * pin 'em up
 *
 * Copyright (C) 2007-2012 by Mario Ködding
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.sourceforge.pinemup.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import net.sourceforge.pinemup.ui.swing.UpdateDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCheckThread extends Thread {
   private static final String UPDATE_URL = "http://pinemup.sourceforge.net/version.php?version=" + PinEmUp.VERSION;
   private static final String UPDATE_DOCUMENT_ENCODING = "UTF-8";

   private static final Logger LOG = LoggerFactory.getLogger(UpdateCheckThread.class);

   private boolean showUpToDateMessage;

   public UpdateCheckThread(boolean showUpToDateMessage) {
      super("Update-Check Thread");
      this.showUpToDateMessage = showUpToDateMessage;
      this.start();
   }

   public void run() {
      try {
         URL url = new URL(UPDATE_URL);
         URLConnection urlc = url.openConnection();
         BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream(), Charset.forName(UPDATE_DOCUMENT_ENCODING)
               .newDecoder()));

         String versionString = br.readLine();
         if (versionString != null && !versionString.equals(PinEmUp.VERSION)) {
            StringBuilder changelogString = new StringBuilder();
            changelogString.append("<html>");
            changelogString.append("<p>" + I18N.getInstance().getString("info.updateavailable.part1") + "</p>");
            changelogString.append("<p>" + I18N.getInstance().getString("info.updateavailable.part2", PinEmUp.VERSION) + "<br />");
            changelogString.append(I18N.getInstance().getString("info.updateavailable.part3", versionString) + "</p>");
            changelogString.append("<p>"
                  + I18N.getInstance().getString("info.updateavailable.part4",
                        "<a href=\"http://pinemup.sourceforge.net\">http://pinemup.sourceforge.net</a>") + "</p>");
            changelogString.append("<p>&nbsp;</p>");
            changelogString.append("<p>Changelog:<br />");
            changelogString.append("--------------------------------</p><p>");
            boolean firstList = true;
            String nextLine;
            do {
               nextLine = br.readLine();
               if (nextLine != null) {
                  if (nextLine.startsWith("-")) {
                     changelogString.append("<li>" + nextLine.substring(2) + "</li>");
                  } else {
                     if (!firstList) {
                        changelogString.append("</ul>");
                     } else {
                        firstList = false;
                     }
                     changelogString.append(nextLine + "<ul>");
                  }

               }
            } while (nextLine != null);
            changelogString.append("</p></html>");

            new UpdateDialog(changelogString.toString());
         } else if (showUpToDateMessage) {
            UserSettings.getInstance().getUserInputRetriever()
                  .showInfoMessageToUser(I18N.getInstance().getString("info.title"), I18N.getInstance().getString("info.versionuptodate"));
         }
         br.close();
      } catch (IOException e) {
         LOG.error("Error while trying to check for updates.", e);
      }

   }
}
