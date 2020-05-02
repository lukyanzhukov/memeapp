import UIKit

protocol ModuleTransitionHandler where Self: UIViewController { }

extension ModuleTransitionHandler {
    
    func show<ModuleType: Assembly>(with model: TransitionModel, moduleType: ModuleType.Type) {
        let view = ModuleType.assembleModule(with: model)
        show(view, sender: nil)
    }
    
    func present<ModuleType: Assembly>(with model: TransitionModel, moduleType: ModuleType.Type) {
        let view = ModuleType.assembleModule(with: model)
        present(view, animated: true, completion: nil)
    }

    func present<ModuleType: Assembly>(moduleType: ModuleType.Type) {
        let view = ModuleType.assembleModule()
        present(view, animated: true, completion: nil)
    }
    
    func push<ModuleType: Assembly>(with model: TransitionModel, moduleType: ModuleType.Type) {
        let view = ModuleType.assembleModule(with: model)
        navigationController?.pushViewController(view, animated: true)
    }

    func push<ModuleType: Assembly>(moduleType: ModuleType.Type) {
        let view = ModuleType.assembleModule()
        navigationController?.pushViewController(view, animated: true)
    }
    
    func closeModule() {
        dismiss(animated: true, completion: nil)
    }
    
}

protocol TransitionModel { }

extension UIViewController: ModuleTransitionHandler { }
