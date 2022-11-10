func userContentController(_ userContentController: WKUserContentController,
                                 didReceive message: WKScriptMessage) {
          guard let body = message.body as? [String: Any] else { return }
          guard let command = body["command"] as? String else { return }
          
        
          if command == "setUserProperty" {
            guard let params = body["parameters"] as? [String: NSObject] else { return }
            
            for (key, value) in params {
                if(key == "user_id"){
                    Analytics.setUserID(value as? String)
                    
                } else{
                    Analytics.setUserProperty(value as? String, forName: key)
                }
            }
          } else if command == "logEvent" {
              guard let name = body["name"] as? String else { return }
              guard let params = body["parameters"] as? [String: NSObject] else { return }
            Analytics.logEvent(name, parameters: params)
          }
        }
