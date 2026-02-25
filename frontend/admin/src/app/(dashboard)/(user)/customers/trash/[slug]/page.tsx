"use client";
import React, { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import CustomerForm from "../../_components/CustomerForm";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";

export default function ViewTrashCustomerPage() {
    const params = useParams();
    const slug = params.slug as string;

    const [initialData, setInitialData] = useState<CustomerResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    useEffect(() => {
        const fetchCustomer = async () => {
            try {
                const res = await customerService.getDeletedById(slug);
                setInitialData(res.data);
            } catch (err) {
                console.error("Lỗi khi tải khách hàng:", err);
                setError(true);
            } finally {
                setLoading(false);
            }
        };
        fetchCustomer();
    }, [slug]);

    if (loading) {
        return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;
    }

    if (error || !initialData) {
        return (
            <div className="p-10 text-center">
                <h2 className="text-xl font-bold text-red-500 mb-2">
                    Không tìm thấy khách hàng!
                </h2>
                <p className="text-gray-500">
                    Khách hàng này có thể đã bị xóa vĩnh viễn hoặc đường dẫn không đúng.
                </p>
            </div>
        );
    }

    return (
        <>
            <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
                Xem chi tiết khách hàng đã xóa
            </h1>

            <Breadcrumb
                className="mb-[30px] ml-[5px]"
                items={[
                    { label: "Tổng quan", href: "/" },
                    { label: "Khách hàng", href: "/customers" },
                    { label: "Thùng rác", href: "/customers/trash" },
                    { label: initialData.fullName || "Chi tiết" }
                ]}
            />

            <CustomerForm initialData={initialData} isReadOnly={true} />
        </>
    )
}
