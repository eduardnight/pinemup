/*
 * pin 'em up
 * 
 * Copyright (C) 2007-2008 by Mario Koedding
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

package net.sourceforge.pinemup.menus;

import java.awt.*;
import java.awt.event.*;
import java.util.ListIterator;

import javax.swing.*;

import net.sourceforge.pinemup.gui.*;
import net.sourceforge.pinemup.logic.*;

public class TrayMenu extends PopupMenu implements ActionListener {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private MenuItem manageCategoriesItem, exportItem, aboutItem, updateItem, closeItem, showSettingsDialogItem, ftpUploadItem, ftpDownloadItem;
   
   private CategoryManager categories;

   public TrayMenu(CategoryManager c) {
      super("pin 'em up");
      categories = c;
      
      //add basic items
      MenuItem[] basicItems = (new MenuCreator(categories)).getBasicMenuItems();
      for (int i=0; i<basicItems.length;i++) {
         add(basicItems[i]);
      }
      addSeparator();
      
      // categories menus
      Menu categoriesMenu = new Menu("category actions");
      add(categoriesMenu);
      Menu[] catMenu = new Menu[categories.getNumberOfCategories()];
      
      //Category menu items
      ListIterator<Category> l = categories.getListIterator();
      int i;
      while (l.hasNext()) {
         i = l.nextIndex();
         catMenu[i] = (new MenuCreator(categories)).getCategoryActionsMenu((i+1) + " " + categories.getCategoryNames()[i],l.next());
         categoriesMenu.add(catMenu[i]);
      }
      
      //other category actions
      categoriesMenu.addSeparator();
      manageCategoriesItem = new MenuItem("manage categories");
      manageCategoriesItem.addActionListener(this);
      categoriesMenu.add(manageCategoriesItem);
      
      // im-/export menu      
      addSeparator();
      Menu imExMenu = new Menu("notes im-/export");
      Menu ftpMenu = new Menu("ftp");
      ftpUploadItem = new MenuItem("upload to ftp server");
      ftpUploadItem.addActionListener(this);
      ftpMenu.add(ftpUploadItem);
      ftpDownloadItem = new MenuItem("download from ftp server");
      ftpDownloadItem.addActionListener(this);
      ftpMenu.add(ftpDownloadItem);
      imExMenu.add(ftpMenu);
      imExMenu.addSeparator();
      exportItem = new MenuItem("export to textfile");
      exportItem.addActionListener(this);
      imExMenu.add(exportItem);
      add(imExMenu);
      
      // other items
      addSeparator();
      showSettingsDialogItem = new MenuItem("settings");
      showSettingsDialogItem.addActionListener(this);
      add(showSettingsDialogItem);
      
      // help menu
      Menu helpMenu = new Menu("help");
      updateItem = new MenuItem("check for updates");
      updateItem.addActionListener(this);
      helpMenu.add(updateItem);
      helpMenu.addSeparator();
      aboutItem = new MenuItem("about pin 'em up");
      aboutItem.addActionListener(this);
      helpMenu.add(aboutItem);
      add(helpMenu);
      addSeparator();
      
      //close item
      closeItem = new MenuItem("exit");
      closeItem.addActionListener(this);
      add(closeItem);
   }

   public void actionPerformed(ActionEvent e) {
      Object src = e.getSource();
      if (src == aboutItem) {
         new AboutDialog();
      } else if (src == showSettingsDialogItem) {
         new SettingsDialog(categories);
      } else if (src == closeItem) {
         PinEmUp.getMainApp().exit();
      } else if (src == ftpUploadItem) {
         if (JOptionPane.showConfirmDialog(null, "Notesfile on server will be replaced, if it already exists! Proceed?","Upload Notesfile",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            //save notes to file
            NoteIO.writeCategoriesToFile(categories);
            //copy file to ftp
            new FTPThread(true,categories);
         }
      } else if (src == ftpDownloadItem) {
         if (JOptionPane.showConfirmDialog(null, "Your current notesfile will be replaced by the version on the FTP server! Proceed?","Download Notesfile",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            new FTPThread(false,categories);
         }
      } else if (src == exportItem) {
         new ExportDialog(categories);
      } else if (src == manageCategoriesItem) {
         new CategoryDialog(categories);
      } else if (src == updateItem) {
         new UpdateCheckThread(true);
      }
      
      // save notes to file after every change
      NoteIO.writeCategoriesToFile(categories);
   }
}
