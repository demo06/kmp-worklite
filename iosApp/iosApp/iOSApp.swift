import UIKit
import common

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)
        let mainViewController = PlatformKt.MainViewController()
        window?.rootViewController = mainViewController
        window?.makeKeyAndVisible()
       let a = NSHomeDirectory()
        print(a)
        return true
    }
}
