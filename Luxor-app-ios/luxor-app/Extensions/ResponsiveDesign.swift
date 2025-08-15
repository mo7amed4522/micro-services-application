import SwiftUI

struct ResponsiveDesign {
    static func padding(for size: CGSize) -> CGFloat {
        let minDimension = min(size.width, size.height)
        return minDimension * 0.04
    }
    
    static func spacing(for size: CGSize) -> CGFloat {
        let minDimension = min(size.width, size.height)
        return minDimension * 0.02
    }
    
    static func cornerRadius(for size: CGSize) -> CGFloat {
        let minDimension = min(size.width, size.height)
        return minDimension * 0.03
    }
    
    static func titleFont(for size: CGSize) -> Font {
        let minDimension = min(size.width, size.height)
        if minDimension < 400 {
            return .title2
        } else if minDimension < 600 {
            return .title
        } else {
            return .largeTitle
        }
    }
    
    static func headlineFont(for size: CGSize) -> Font {
        let minDimension = min(size.width, size.height)
        if minDimension < 400 {
            return .subheadline
        } else {
            return .headline
        }
    }
    
    static func bodyFont(for size: CGSize) -> Font {
        let minDimension = min(size.width, size.height)
        if minDimension < 400 {
            return .caption
        } else {
            return .body
        }
    }
    
    static func captionFont(for size: CGSize) -> Font {
        let minDimension = min(size.width, size.height)
        if minDimension < 400 {
            return .caption2
        } else {
            return .caption
        }
    }
    
    static func buttonHeight(for size: CGSize) -> CGFloat {
        let minDimension = min(size.width, size.height)
        return minDimension * 0.12
    }
    
    static func iconFont(for size: CGSize) -> Font {
        let minDimension = min(size.width, size.height)
        let iconSize = minDimension * 0.06
        return .system(size: iconSize)
    }
    
    // Device-specific adjustments
    static var isIPad: Bool {
        UIDevice.current.userInterfaceIdiom == .pad
    }
    
    static var isIPhone: Bool {
        UIDevice.current.userInterfaceIdiom == .phone
    }
}