import UIKit
import ClientCommon

final class SplashController: UIViewController, ViewSpecificController {
    
    typealias RootView = SplashView
    
    var viewModel: SplashViewModel!
    
    override func loadView() {
        view = SplashView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        let route = viewModel.getRoute()
        
        switch route {
        case _ as SplashNavigation.ToAuth:
            present(moduleType: AuthAssembly.self)
        case _ as SplashNavigation.ToMain:
            print("Go to main")
        default:
            break
        }
    }
}
