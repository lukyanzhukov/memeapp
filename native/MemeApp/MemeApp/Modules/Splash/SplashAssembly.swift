import ClientCommon
import UIKit.UIViewController

final class SplashAssembly: Assembly {
    static func assembleModule() -> UIViewController {
        let tokenSource = Settings.tokenSource
        let viewModel = SplashViewModel(tokenSource: tokenSource)
        
        let view = SplashController()
        view.viewModel = viewModel
        
        return view
    }
}
