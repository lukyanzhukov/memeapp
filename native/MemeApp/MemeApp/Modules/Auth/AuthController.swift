import UIKit
import ClientCommon

final class AuthController: UIViewController, ViewSpecificController {
    
    typealias RootView = AuthView
    
    var viewModel: AuthViewModel!
    var labelTimer: Timer?
    
    override func loadView() {
        view = AuthView()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        targeting()
        observing()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        view().endEditing(true)
    }
    
    private func observing() { // swiftlint:disable:this cyclomatic_complexity
        viewModel.loading.observe { [weak self] isLoading in
            guard let isLoading = isLoading as? Bool else { return }
            isLoading ? self?.view().loadingView.startAnimating() : self?.view().loadingView.stopAnimating()
        }
        
        viewModel.authResult.observe { [weak self] authResult in
            guard let self = self else { return }
            guard let authResult = authResult as? AuthResult else { return }
            switch authResult {
            case .FailEmptyPassword(): // swiftlint:disable:this empty_enum_arguments
                self.view().failureAnimation(for: self.view().passwordTextField)
                self.view().errorText = "Пароль не может быть пустым"
                
            case .FailInvalidPassword(): // swiftlint:disable:this empty_enum_arguments
                self.view().failureAnimation(for: self.view().passwordTextField)
                self.view().errorText = "Неверный пароль"
                
            case .FailInvalidLogin(): // swiftlint:disable:this empty_enum_arguments
                self.view().failureAnimation(for: self.view().loginTextField)
                self.view().errorText = "Не правильно введен логин"
                
            case .FailNetworkError(): // swiftlint:disable:this empty_enum_arguments
                self.view().errorText = "Нет интернета, проверьте ваше соединение"
                
            case .FailUserAlreadyRegistered(): // swiftlint:disable:this empty_enum_arguments
                self.view().failureAnimation(for: self.view().loginTextField)
                self.view().errorText = "Такой пользователь уже существует"
                
            case .FailUserNotFound(): // swiftlint:disable:this empty_enum_arguments
                self.view().failureAnimation(for: self.view().loginTextField)
                self.view().errorText = "Пользователь не найден"
                
            case .Success():
                print("go to main")
                
            default:
                break
            }
        }
    }
    
    private func targeting() {
        view().loginButton.addTarget(self, action: #selector(authButtonDidTapped(_:)), for: .touchUpInside)
        view().registerButton.addTarget(self, action: #selector(registerButtonDidTapped(_:)), for: .touchUpInside)
    }
    
    @objc private func authButtonDidTapped(_ sender: UIButton) {
        viewModel.auth(login: view().loginTextField.text ?? "", password: view().passwordTextField.text ?? "")
    }
    
    @objc private func registerButtonDidTapped(_ sender: UIButton) {
        viewModel.register(login: view().loginTextField.text ?? "", password: view().passwordTextField.text ?? "")
    }
}
