import Nibless

final class AuthView: NLView {
    
    let logoImageView = UIImageView()
    
    let loadingView: UIActivityIndicatorView = {
        let view = UIActivityIndicatorView()
        view.hidesWhenStopped = true
        return view
    }()
    
    let loginTextField: UITextField = {
        let textField = UITextField()
        textField.borderStyle = .roundedRect
        textField.placeholder = "Email"
        textField.backgroundColor = .clear
        return textField
    }()
    
    let passwordTextField: UITextField = {
        let textField = UITextField()
        textField.borderStyle = .roundedRect
        textField.placeholder = "Пароль"
        textField.backgroundColor = .clear
        textField.isSecureTextEntry = true
        return textField
    }()
    
    let failureLabel: UILabel = {
        let label = UILabel()
        label.textColor = .red
        return label
    }()
    
    let loginButton: UIButton = {
        let button = UIButton()
        button.backgroundColor = UIColor(named: "accentColor")
        button.layer.cornerRadius = 5
        button.setTitle("Вход", for: .normal)
        button.setTitleColor(.white, for: .normal)
        return button
    }()
    
    let registerButton: UIButton = {
        let button = UIButton()
        button.backgroundColor = UIColor(named: "accentColor")
        button.layer.cornerRadius = 5
        button.setTitle("Регистрация", for: .normal)
        button.setTitleColor(.white, for: .normal)
        return button
    }()
    
    var errorText: String = "" {
        didSet {
            failureLabel.text = errorText
            failureLabel.sizeToFit()
            UIView.animate(withDuration: 0.2) { [weak self] in
                self?.failureLabel.isHidden = false
            }
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor(named: "backgroundColor")
        
        addSubview(logoImageView)
        addSubview(loginTextField)
        addSubview(passwordTextField)
        addSubview(failureLabel)
        addSubview(loginButton)
        addSubview(registerButton)
    }
    
    override func layoutSubviews() {
        loginTextField.frame.size = CGSize(width: frame.width - 40, height: 50)
        loginTextField.center = CGPoint(x: center.x, y: center.y - 30)
        
        passwordTextField.frame.size = loginTextField.frame.size
        passwordTextField.center = CGPoint(x: center.x, y: center.y + 30)
        
        failureLabel.frame.origin.y = passwordTextField.frame.maxY + 20
        failureLabel.center.x = center.x
        
        registerButton.frame.size = loginTextField.frame.size
        registerButton.center = CGPoint(x: center.x, y: frame.height - safeAreaInsets.bottom - safeAreaInsets.top - 20)
        
        loginButton.frame.size = loginTextField.frame.size
        loginButton.center = CGPoint(x: center.x, y: registerButton.center.y - 60)
    }
    
    func failureAnimation(for textField: UITextField) {
        UIView.animate(withDuration: 0.2, animations: {
            textField.textColor = .red
            textField.center.x -= 5
            }, completion: { _ in
                UIView.animate(withDuration: 0.2, animations: {
                    textField.center.x += 10
                }, completion: { _ in
                    UIView.animate(withDuration: 0.2) {
                        textField.textColor = .black
                        textField.center.x -= 5
                    }
                })
        })
        
    }
}
