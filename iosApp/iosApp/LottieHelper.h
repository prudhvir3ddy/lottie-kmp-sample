#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface LottieHelper : NSObject

+ (UIView * _Nonnull)createLottieView;
+ (void)loadAnimation:(UIView * _Nonnull)view url:(NSString * _Nonnull)url iterations:(NSInteger)iterations;
+ (void)disposeAnimation:(UIView * _Nonnull)view;

@end