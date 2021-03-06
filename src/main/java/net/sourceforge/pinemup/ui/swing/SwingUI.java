package net.sourceforge.pinemup.ui.swing;

import java.awt.AWTException;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import net.sourceforge.pinemup.core.I18N;
import net.sourceforge.pinemup.core.UserSettings;
import net.sourceforge.pinemup.ui.PinEmUpUI;
import net.sourceforge.pinemup.ui.swing.menus.TrayMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingUI extends PinEmUpUI {
   private static final Logger LOG = LoggerFactory.getLogger(SwingUI.class);

   private TrayMenu trayMenu;

   @Override
   public void initialize() {
      if (SystemTray.isSupported()) {
         // add trayicon
         SystemTray tray = SystemTray.getSystemTray();
         try {
            trayMenu = new TrayMenu();
            tray.add(new PinEmUpTrayIcon(trayMenu));
         } catch (AWTException e) {
            LOG.error("Error during initialization of tray icon.", e);
         }

         // define user interface for retrieving user input, such as passwords
         // or filenames
         UserSettings.getInstance().setUserInputRetriever(new SwingUserInputRetreiver());
      } else {
         // tray icon not supported
         JOptionPane.showMessageDialog(null, I18N.getInstance().getString("error.trayiconnotsupported"),
               I18N.getInstance().getString("error.title"), JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
   }

   @Override
   public void refreshCategories() {
      trayMenu.createCategoriesMenu();
   }

   @Override
   public void refreshI18NStrings() {
      trayMenu.init();
   }
}
