"use client";
import React, { Component, ReactNode } from "react";

interface ErrorBoundaryProps {
  children: ReactNode;
  fallback?: ReactNode;
}

interface ErrorBoundaryState {
  hasError: boolean;
  error: Error | null;
}

export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error("ErrorBoundary caught:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      if (this.props.fallback) {
        return this.props.fallback;
      }
      return (
        <div className="flex flex-col items-center justify-center py-[60px] px-[20px]">
          <div className="bg-red-50 border border-red-200 rounded-[12px] p-[32px] max-w-[480px] text-center">
            <h2 className="text-[18px] font-bold text-red-600 mb-[8px]">Đã xảy ra lỗi</h2>
            <p className="text-[14px] text-red-500 mb-[16px]">
              {this.state.error?.message || "Có lỗi không mong muốn xảy ra."}
            </p>
            <button
              type="button"
              className="px-[24px] py-[10px] bg-red-600 text-white rounded-[8px] font-[600] text-[14px]
                hover:bg-red-700 transition-colors cursor-pointer"
              onClick={() => this.setState({ hasError: false, error: null })}
            >
              Thử lại
            </button>
          </div>
        </div>
      );
    }
    return this.props.children;
  }
}
