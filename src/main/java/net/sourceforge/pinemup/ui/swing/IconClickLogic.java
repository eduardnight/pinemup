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

package net.sourceforge.pinemup.ui.swing;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import net.sourceforge.pinemup.core.CategoryManager;
import net.sourceforge.pinemup.core.Note;

class IconClickLogic extends MouseAdapter implements ActionListener {
   private Timer timer;

   private static final int CLICK_INTERVAL = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");

   @Override
   public void actionPerformed(ActionEvent arg0) {
      timer.stop();
      singleClick();
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) {
         if (e.getClickCount() == 1) {
            timer = new Timer(CLICK_INTERVAL, this);
            timer.start();
         } else {
            timer.stop();
            if (e.getClickCount() == 2) {
               doubleClick();
            }
         }
      }
   }

   private void singleClick() {
      NoteWindowManager.getInstance().bringAllWindowsToFront();
   }

   private void doubleClick() {
      Note newNote = CategoryManager.getInstance().createNoteAndAddToDefaultCategory();
      NoteWindow window = NoteWindowManager.getInstance().createNoteWindowForNote(newNote);
      window.jumpIntoTextArea();
   }
}
