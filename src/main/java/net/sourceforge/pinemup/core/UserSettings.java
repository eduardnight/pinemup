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

import java.io.File;
import java.util.Locale;
import java.util.prefs.Preferences;

import net.sourceforge.pinemup.core.I18N.SupportedLocale;
import net.sourceforge.pinemup.io.NotesFileManager;
import net.sourceforge.pinemup.io.server.ServerConnection.ConnectionType;
import net.sourceforge.pinemup.ui.UserInputRetriever;

public final class UserSettings {
   private static final String PREFIX = "peu_dev_";
   private static final int MIN_NOTEWINDOW_WIDTH = 30;
   private static final int MIN_NOTEWINDOW_HEIGHT = 30;

   private Preferences prefs;
   private UserInputRetriever userInputRetriever;

   private short defaultWindowWidth;
   private short defaultWindowHeight;
   private short defaultWindowXPosition;
   private short defaultWindowYPosition;
   private short defaultFontSize;
   private String notesFile;
   private String serverAddress;
   private String serverUser;
   private String serverPasswd;
   private String serverDir;
   private boolean defaultAlwaysOnTop;
   private byte tempDef = 0;
   private byte closeicon;
   private boolean showCategory;
   private boolean confirmDeletion;
   private boolean storeServerPass;
   private boolean updateCheckEnabled;
   private ConnectionType serverType;
   private Locale locale;
   private boolean confirmUpDownload;

   private static class Holder {
      private static final UserSettings INSTANCE = new UserSettings();
   }

   public static UserSettings getInstance() {
      return Holder.INSTANCE;
   }

   public boolean getConfirmUpDownload() {
      return confirmUpDownload;
   }

   public void setConfirmUpDownload(boolean b) {
      confirmUpDownload = b;
   }

   public void setServerType(ConnectionType st) {
      serverType = st;
   }

   public ConnectionType getServerType() {
      return serverType;
   }

   public void setUpdateCheckEnabled(boolean b) {
      updateCheckEnabled = b;
   }

   public boolean isUpdateCheckEnabled() {
      return updateCheckEnabled;
   }

   public void setConfirmDeletion(boolean b) {
      confirmDeletion = b;
   }

   public boolean getConfirmDeletion() {
      return confirmDeletion;
   }

   public void setShowCategory(boolean sc) {
      showCategory = sc;
   }

   public boolean getShowCategory() {
      return showCategory;
   }

   public byte getCloseIcon() {
      return closeicon;
   }

   public void setCloseIcon(byte ci) {
      closeicon = ci;
   }

   public String getNotesFile() {
      return notesFile;
   }

   public void setNotesFile(String s) {
      notesFile = s;
   }

   public boolean getDefaultAlwaysOnTop() {
      return defaultAlwaysOnTop;
   }

   public void setDefaultAlwaysOnTop(boolean b) {
      defaultAlwaysOnTop = b;
   }

   public short getDefaultFontSize() {
      return defaultFontSize;
   }

   public void setDefaultFontSize(short size) {
      defaultFontSize = size;
   }

   public byte getTempDef() {
      return tempDef;
   }

   public void setTempDef(byte td) {
      tempDef = td;
   }

   public short getDefaultWindowWidth() {
      return defaultWindowWidth;
   }

   public void setDefaultWindowWidth(short x) {
      if (x < MIN_NOTEWINDOW_WIDTH) {
         x = MIN_NOTEWINDOW_WIDTH;
      }
      defaultWindowWidth = x;
   }

   public short getDefaultWindowHeight() {
      return defaultWindowHeight;
   }

   public void setDefaultWindowHeight(short y) {
      if (y < MIN_NOTEWINDOW_HEIGHT) {
         y = MIN_NOTEWINDOW_HEIGHT;
      }
      defaultWindowHeight = y;
   }

   public short getDefaultWindowXPostition() {
      return defaultWindowXPosition;
   }

   public void setDefaultWindowXPosition(short x) {
      defaultWindowXPosition = x;
   }

   public short getDefaultWindowYPostition() {
      return defaultWindowYPosition;
   }

   public void setDefaultWindowYPosition(short y) {
      defaultWindowYPosition = y;
   }

   public String getServerAddress() {
      return serverAddress;
   }

   public void setServerAddress(String s) {
      serverAddress = s;
   }

   public String getServerUser() {
      return serverUser;
   }

   public void setServerUser(String u) {
      serverUser = u;
   }

   public String getServerPasswd() {
      String tempString = "";
      if (storeServerPass) {
         tempString = serverPasswd;
      } else {
         if (userInputRetriever != null) {
            tempString = userInputRetriever.retrievePasswordFromUser();
         }
      }
      return tempString;
   }

   public void setServerPasswdFromCharArray(char[] passwdCharArray) {
      if (passwdCharArray != null) {
         serverPasswd = String.valueOf(passwdCharArray);
      } else {
         serverPasswd = "";
      }
   }

   public String getServerDir() {
      return serverDir;
   }

   public void setServerDir(String d) {
      serverDir = d;
   }

   public void setStoreServerPass(boolean b) {
      storeServerPass = b;
   }

   public boolean getStoreServerPass() {
      return storeServerPass;
   }

   public Locale getLocale() {
      return locale;
   }

   public void setLocale(Locale l) {
      locale = l;
   }

   public void saveSettings() {
      // save preferences
      prefs.putInt(PREFIX + "defaultWindowWidth", defaultWindowWidth);
      prefs.putInt(PREFIX + "defaultWindowHeight", defaultWindowHeight);
      prefs.putInt(PREFIX + "defaultWindowXPosition", defaultWindowXPosition);
      prefs.putInt(PREFIX + "defaultWindowYPosition", defaultWindowYPosition);
      prefs.putInt(PREFIX + "defaultFontSize", defaultFontSize);
      prefs.putBoolean(PREFIX + "defaultAlwaysOnTop", defaultAlwaysOnTop);
      prefs.put(PREFIX + "notesFile", notesFile);
      prefs.put(PREFIX + "serverAddress", serverAddress);
      prefs.put(PREFIX + "serverUser", serverUser);
      if (storeServerPass) {
         prefs.put(PREFIX + "serverPasswd", getServerPasswd());
      } else {
         prefs.put(PREFIX + "serverPasswd", "");
      }
      prefs.put(PREFIX + "serverDir", serverDir);
      prefs.putInt(PREFIX + "closeicon", closeicon);
      prefs.putBoolean(PREFIX + "showCategory", showCategory);
      prefs.putBoolean(PREFIX + "confirmDeletion", confirmDeletion);
      prefs.putBoolean(PREFIX + "storeServerPass", storeServerPass);
      prefs.putBoolean(PREFIX + "updateCheckEnabled", updateCheckEnabled);
      prefs.putInt(PREFIX + "serverType", serverType.getCode());
      prefs.put(PREFIX + "locale", locale.toString());
      prefs.putBoolean(PREFIX + "confirmUpDownload", confirmUpDownload);
   }

   private UserSettings() {
      prefs = Preferences.userNodeForPackage(PinEmUp.class);

      String homeDir = System.getProperty("user.home");
      if (homeDir.charAt(homeDir.length() - 1) != '\\' && homeDir.charAt(homeDir.length() - 1) != '/') {
         homeDir = homeDir + "/";
      }

      defaultWindowWidth = Short.parseShort(prefs.get(PREFIX + "defaultWindowWidth", "170"));
      defaultWindowHeight = Short.parseShort(prefs.get(PREFIX + "defaultWindowHeight", "150"));
      defaultWindowXPosition = Short.parseShort(prefs.get(PREFIX + "defaultWindowXPosition", "0"));
      defaultWindowYPosition = Short.parseShort(prefs.get(PREFIX + "defaultWindowYPosition", "0"));
      defaultFontSize = Short.parseShort(prefs.get(PREFIX + "defaultFontSize", "14"));
      defaultAlwaysOnTop = prefs.getBoolean(PREFIX + "defaultAlwaysOnTop", false);
      notesFile = prefs.get(PREFIX + "notesFile", homeDir + "pinemup.xml");
      serverAddress = prefs.get(PREFIX + "serverAddress", "ftp.example.com");
      serverUser = prefs.get(PREFIX + "serverUser", "anonymous");
      serverPasswd = prefs.get(PREFIX + "serverPasswd", "");
      serverDir = prefs.get(PREFIX + "serverDir", "/");
      closeicon = Byte.parseByte(prefs.get(PREFIX + "closeicon", "1"));
      showCategory = prefs.getBoolean(PREFIX + "showCategory", false);
      confirmDeletion = prefs.getBoolean(PREFIX + "confirmDeletion", true);
      storeServerPass = prefs.getBoolean(PREFIX + "storeServerPass", false);
      updateCheckEnabled = prefs.getBoolean(PREFIX + "updateCheckEnabled", true);
      serverType = ConnectionType.getConnectionTypeByCode(Short.parseShort(prefs.get(PREFIX + "serverType", "0")));
      String localeString = prefs.get(PREFIX + "locale", "en_US");
      locale = SupportedLocale.fromLocaleString(localeString).getLocale();
      confirmUpDownload = prefs.getBoolean(PREFIX + "confirmUpDownload", true);
   }

   public void makeSureNotesFileIsValid() {
      String validFilePath = notesFile;
      File nfile = new File(validFilePath);
      while (nfile.exists() && !NotesFileManager.getInstance().fileIsValid(validFilePath)) {
         if (!userInputRetriever.retrieveUserConfirmation(I18N.getInstance().getString("error.title"),
               I18N.getInstance().getString("error.notesfilenotvalid"))) {
            System.exit(0);
         }

         File selectedFile = userInputRetriever.retieveFileChoiceFromUser();
         if (selectedFile != null) {
            validFilePath = NotesFileManager.checkAndAddExtension(selectedFile.getAbsolutePath(), ".xml");
            nfile = new File(validFilePath);
         }
      }
      notesFile = validFilePath;
   }

   public void setUserInputRetriever(UserInputRetriever userInputRetriever) {
      this.userInputRetriever = userInputRetriever;
   }

   public UserInputRetriever getUserInputRetriever() {
      return userInputRetriever;
   }
}
