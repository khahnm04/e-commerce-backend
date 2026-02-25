"use client";
import { useEffect, useState } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import EmployeeForm from "../../_components/EmployeeForm";
import { employeeService } from "@/services/user/employee.service";
import { EmployeeResponse } from "@/types/user/employee.types";
import { use } from "react";

export default function EmployeeTrashDetailPage({
    params,
}: {
    params: Promise<{ slug: string }>;
}) {
    const { slug } = use(params);
    const [data, setData] = useState<EmployeeResponse | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await employeeService.getDeletedById(slug);
                setData(res.data);
            } catch {
                console.error("Không thể tải dữ liệu nhân viên");
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [slug]);

    if (loading) {
        return (
            <div className="text-center py-10 text-[var(--color-text)]">
                Đang tải dữ liệu...
            </div>
        );
    }

    if (!data) {
        return (
            <div className="p-10 text-center">
                <h2 className="text-xl font-bold text-red-500 mb-2">
                    Không tìm thấy nhân viên!
                </h2>
                <p className="text-gray-500">
                    Nhân viên này không tồn tại trong thùng rác hoặc đường dẫn không đúng.
                </p>
            </div>
        );
    }

    return (
        <main>
            <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
                Chi tiết nhân viên (Thùng rác)
            </h1>

            <Breadcrumb
                className="mb-[30px] ml-[5px]"
                items={[
                    { label: "Tổng quan", href: "/" },
                    { label: "Nhân viên", href: "/employees" },
                    { label: "Thùng rác", href: "/employees/trash" },
                    { label: data.fullName || "Chi tiết" },
                ]}
            />

            <EmployeeForm initialData={data} isReadOnly={true} />
        </main>
    );
}
