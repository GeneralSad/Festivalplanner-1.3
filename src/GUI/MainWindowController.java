package GUI;

import GUI.MainWindow;

public class MainWindowController
{
    private MainWindow mainWindow;

    public MainWindowController(MainWindow mainWindow)
    {
        this.mainWindow = mainWindow;
    }

    public MainWindowController()
    {
    }

    public void setMainWindow(MainWindow mainWindow)
    {
        this.mainWindow = mainWindow;
    }

    public void update() {
        if (mainWindow != null) {
            mainWindow.update();
        }
    }
}
