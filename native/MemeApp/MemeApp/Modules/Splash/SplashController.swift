import UIKit
import ClientCommon

final class SplashController: UIViewController, ViewSpecificController {
    
    typealias RootView = SplashView
    
    var viewModel: SplashViewModel!
    
    override func loadView() {
        view = SplashView()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let route = viewModel.getRoute()
        
        switch route {
        case _ as SplashNavigation.ToAuth:
            print("Go to auth")
        case _ as SplashNavigation.ToMain:
            print("Go to main")
        default:
            break
        }
    }
}
