//
//  ViewController.swift
//  MemeApp
//
//  Created by Василий Спитченко on 12.04.2020.
//  Copyright © 2020 MemeApp. All rights reserved.
//

import UIKit
import ClientCommon

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let tokenSource = SettingsTokenSource(settings: AppleSettings(name: nil))
        let viewModel = SplashViewModel(tokenSource: tokenSource)
    
        let route = viewModel.getRoute()
        
        switch route {
        case _ as SplashNavigation.ToAuth:
            print("Go to auth")
        case _ as SplashNavigation.ToMain:
            print("Go to main")
        default:
            print()
        }
        
        let httpClient = ClientKt.MemeClient(tokenSource: tokenSource)
        let ratingModel = RatingViewModel(httpClient: httpClient)
        
        /*ratingModel.state.observe { (state: Any) in
            switch state {
            case _ as RatingState.Fail:
                print("Fail")
            case _ as RatingState.Progress:
                print("Progress")
            case let data as RatingState.Success:
                print("Success \(data.rating)")
            default:
                print()
            }
        }
        
        ratingModel.getRating()*/
        
        let memeBattleViewModel = MemeBattleViewModel(client: httpClient, tokenSource: tokenSource)
        
        memeBattleViewModel.state.observe { (state: Any) in
            switch state {
            case let error as MemeBattleState.Error:
                print("Fail \(error.throwable ?? "null")")
            case _ as MemeBattleState.Progress:
                print("Progress")
            case let data as MemeBattleState.Meme:
                print("Success \(data)")
            default:
                print()
            }
        }
        
        memeBattleViewModel.connect()
    }
}

