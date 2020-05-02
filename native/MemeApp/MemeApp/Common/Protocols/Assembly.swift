import UIKit

protocol Assembly {
    static func assembleModule() -> UIViewController
    static func assembleModule(with model: TransitionModel) -> UIViewController
    static func assemble<ViewController: UIViewController>(with model: TransitionModel) -> ViewController
}

/// This extension make all methods optional without using @objc
extension Assembly {
    static func assembleModule() -> UIViewController {
        fatalError("Implement assembleModule() in ModuleAssembly")
    }
    
    static func assembleModule(with model: TransitionModel) -> UIViewController {
        fatalError("Implement assembleModule(with model: TransitionModel) in ModuleAssembly")
    }
    
    static func assemble<ViewController: UIViewController>(with model: TransitionModel) -> ViewController {
        assembleModule(with: model) as! ViewController // swiftlint:disable:this force_cast
    }
}
