"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import { FaChevronDown } from "react-icons/fa";

interface Option {
  id: number | string;
  name: string;
}

interface CustomSelectProps {
  options: Option[];
  value: string | number;
  onChange: (val: string | number) => void;
  placeholder?: string;
  classNameCustom?: string;
  disabled?: boolean;
}

export const CustomSelect = ({
  options,
  value,
  onChange,
  placeholder = "Chọn...",
  classNameCustom = "",
  disabled = false,
}: CustomSelectProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [openUp, setOpenUp] = useState(false);
  const [highlightIndex, setHighlightIndex] = useState(-1);

  const containerRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLDivElement>(null);

  const selectedLabel =
    value !== "" ? options.find((opt) => String(opt.id) === String(value))?.name : undefined;

  // Click outside -> close dropdown
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(event.target as Node)) {
        setIsOpen(false);
        setHighlightIndex(-1);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  // Reset highlight when dropdown opens
  useEffect(() => {
    if (isOpen) {
      const currentIdx = options.findIndex((opt) => String(opt.id) === String(value));
      setHighlightIndex(currentIdx >= 0 ? currentIdx : 0);
    }
  }, [isOpen, options, value]);

  const handleToggle = () => {
    if (disabled) return;

    if (!isOpen && triggerRef.current) {
      const rect = triggerRef.current.getBoundingClientRect();
      const DROPDOWN_MAX_HEIGHT = 300;

      const spaceBelow = window.innerHeight - rect.bottom;
      const spaceAbove = rect.top;

      setOpenUp(spaceBelow < DROPDOWN_MAX_HEIGHT && spaceAbove > spaceBelow);
    }

    setIsOpen((prev) => !prev);
  };

  const handleSelect = useCallback(
    (optId: string | number) => {
      onChange(optId);
      setIsOpen(false);
      setHighlightIndex(-1);
    },
    [onChange],
  );

  // Keyboard navigation
  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (disabled) return;

      switch (e.key) {
        case "Enter":
        case " ":
          e.preventDefault();
          if (isOpen && highlightIndex >= 0 && highlightIndex < options.length) {
            handleSelect(options[highlightIndex].id);
          } else {
            handleToggle();
          }
          break;
        case "Escape":
          setIsOpen(false);
          setHighlightIndex(-1);
          break;
        case "ArrowDown":
          e.preventDefault();
          if (!isOpen) {
            handleToggle();
          } else {
            setHighlightIndex((prev) => (prev < options.length - 1 ? prev + 1 : 0));
          }
          break;
        case "ArrowUp":
          e.preventDefault();
          if (isOpen) {
            setHighlightIndex((prev) => (prev > 0 ? prev - 1 : options.length - 1));
          }
          break;
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [disabled, isOpen, highlightIndex, options, handleSelect],
  );

  return (
    <div
      ref={containerRef}
      className={`relative w-fit max-w-full ${classNameCustom}`}
      onKeyDown={handleKeyDown}
      role="listbox"
      aria-expanded={isOpen}
      aria-label={placeholder}
    >
      {/* Trigger */}
      <div
        ref={triggerRef}
        onClick={handleToggle}
        tabIndex={disabled ? -1 : 0}
        className="
          flex items-center gap-2 cursor-pointer
          w-full h-full
          py-2 px-1
          font-[700] text-[14px]
          hover:opacity-70 transition-opacity
          overflow-hidden
          outline-none focus-visible:ring-2 focus-visible:ring-blue-400 rounded
        "
      >
        <span className="whitespace-nowrap overflow-hidden text-ellipsis">
          {selectedLabel || placeholder}
        </span>

        <FaChevronDown
          className={`
            text-[10px] opacity-60
            transition-transform duration-200
            ${isOpen ? "rotate-180" : ""}
          `}
        />
      </div>

      {/* Dropdown */}
      {isOpen && (
        <div
          className={`
            absolute left-0
            ${openUp ? "bottom-full mb-1" : "top-full mt-1"}
            w-max min-w-full max-w-[300px]
            bg-white border border-[#E5E7EB]
            rounded-[8px] shadow-xl
            max-h-[300px] overflow-y-auto
            animate-fade-in-up z-[999]
          `}
          role="listbox"
        >
          {options.map((opt, idx) => (
            <div
              key={opt.id}
              role="option"
              aria-selected={String(value) === String(opt.id)}
              onClick={() => handleSelect(opt.id)}
              className={`
                px-4 py-2.5 cursor-pointer text-[14px] font-medium
                transition-colors border-b border-gray-50
                last:border-none whitespace-nowrap
                ${String(value) === String(opt.id)
                  ? "text-blue-600 bg-blue-50"
                  : idx === highlightIndex
                    ? "bg-gray-100 text-black"
                    : "text-gray-600 hover:bg-gray-50 hover:text-black"
                }
              `}
            >
              {opt.name}
            </div>
          ))}

          {options.length === 0 && (
            <div className="px-4 py-3 text-gray-400 text-[13px] italic text-center whitespace-nowrap">
              Không có dữ liệu
            </div>
          )}
        </div>
      )}
    </div>
  );
};
