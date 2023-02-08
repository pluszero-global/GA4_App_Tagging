- (void)userContentController:(WKUserContentController *)userContentController
      didReceiveScriptMessage:(WKScriptMessage *)message {
  if ([message.body[@"command"] isEqual:@"setUserProperty"]) {
      for (id key in message.body[@"parameters"]){
          if([key isEqual:@"user_id"]){
              printf("user_id   : %s \n",[[message.body[@"parameters"] objectForKey:key] UTF8String] );
              [FIRAnalytics setUserID:[message.body[@"parameters"] objectForKey:key]];
          }
          else{
             
              [FIRAnalytics setUserPropertyString:[message.body[@"parameters"] objectForKey:key] forName:key];
          }
      }
      
  } else if ([message.body[@"command"] isEqual: @"logEvent"]) {
      [FIRAnalytics logEventWithName:message.body[@"name"] parameters:message.body[@"parameters"]];

  }
}
