"use client";
import { useState } from "react";

export const useTableManager = <T,>() => {
  const [searchTerm, setSearchTerm] = useState("");
  const [actionFilter, setActionFilter] = useState<string | number>("");

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState<T | null>(null);

  // Filter States
  const [statusFilter, setStatusFilter] = useState<string | number>("");
  const [creatorFilter, setCreatorFilter] = useState<string | number>("");

  // Hàm mở modal xóa
  const handleOpenDeleteModal = (item: T) => {
    setSelectedItem(item);
    setIsModalOpen(true);
  };

  // Hàm đóng modal
  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedItem(null);
  };

  const handleConfirmDelete = () => {
    // Sau khi gọi API thành công:
    setIsModalOpen(false);
    setSelectedItem(null);
  };

  return {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    creatorFilter, setCreatorFilter,
    isModalOpen,
    selectedItem,
    handleOpenDeleteModal,
    handleCloseModal,
  };
};
