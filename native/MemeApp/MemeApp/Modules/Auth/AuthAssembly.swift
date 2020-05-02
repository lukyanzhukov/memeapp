import UIKit
import ClientCommon

final class AuthAssembly: Assembly {
    static func assembleModule() -> UIViewController {
        let tokenSource = Settings.tokenSource
        let loginSource = Settings.loginSource
        let client = ClientKt.MemeClient(tokenSource: tokenSource)
        let viewModel = AuthViewModel(client: client, tokenSource: tokenSource, loginSource: loginSource)
        
        let view = AuthController()
        view.viewModel = viewModel
        
        view.modalPresentationStyle = .fullScreen
        view.modalTransitionStyle = .crossDissolve
        
        return view
    }
}
