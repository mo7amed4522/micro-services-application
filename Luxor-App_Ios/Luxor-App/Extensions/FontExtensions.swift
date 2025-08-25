//
//  FontExtensions.swift
//  Luxor-App
//
//  Created by Assistant on $(date)
//

import SwiftUI

// MARK: - Font Extensions for Luxor Theme
extension Font {
    static var luxorTitle: Font {
        .system(size: 28, weight: .bold, design: .default)
    }
    
    static var luxorTitle2: Font {
        .system(size: 22, weight: .bold, design: .default)
    }
    
    static var luxorHeadline: Font {
        .system(size: 18, weight: .semibold, design: .default)
    }
    
    static var luxorBody: Font {
        .system(size: 16, weight: .regular, design: .default)
    }
    
    static var luxorBodyBold: Font {
        .system(size: 16, weight: .bold, design: .default)
    }
    
    static var luxorCaption: Font {
        .system(size: 12, weight: .regular, design: .default)
    }
}