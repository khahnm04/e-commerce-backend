"use client";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { toast } from "react-hot-toast";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { customerService } from "@/services/user/customer.service";

const ChangePasswordSchema = z
    .object({
        currentPassword: z.string().min(1, "Mật khẩu hiện tại không được để trống"),
        newPassword: z.string().min(6, "Mật khẩu mới phải ít nhất 6 ký tự"),
        confirmNewPassword: z.string().min(1, "Xác nhận mật khẩu không được để trống"),
    })
    .refine((data) => data.newPassword === data.confirmNewPassword, {
        message: "Mật khẩu mới và xác nhận không khớp",
        path: ["confirmNewPassword"],
    });

type ChangePasswordValues = z.infer<typeof ChangePasswordSchema>;

interface ChangePasswordFormProps {
    customerId: number;
}

export default function ChangePasswordForm({ customerId }: ChangePasswordFormProps) {
    const [isLoading, setIsLoading] = useState(false);

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm<ChangePasswordValues>({
        resolver: zodResolver(ChangePasswordSchema),
        defaultValues: {
            currentPassword: "",
            newPassword: "",
            confirmNewPassword: "",
        },
    });

    const onSubmit = async (data: ChangePasswordValues) => {
        try {
            setIsLoading(true);
            await customerService.changePassword(customerId, {
                currentPassword: data.currentPassword,
                newPassword: data.newPassword,
                confirmNewPassword: data.confirmNewPassword,
            });
            toast.success("Đổi mật khẩu thành công!");
            reset();
        } catch (error: unknown) {
            toast.error(error instanceof Error ? error.message : "Có lỗi xảy ra!");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px] mt-[30px]">
            <h2 className="text-[20px] font-[700] text-[var(--color-text)] mb-[30px]">
                Đổi mật khẩu
            </h2>
            <form
                className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
                onSubmit={(e) => handleSubmit(onSubmit)(e).catch(() => { })}
            >
                <div className="md:col-span-2">
                    <FormInput
                        label="Mật khẩu hiện tại"
                        id="currentPassword"
                        type="password"
                        {...register("currentPassword")}
                        error={errors.currentPassword?.message}
                    />
                </div>

                <FormInput
                    label="Mật khẩu mới"
                    id="newPassword"
                    type="password"
                    {...register("newPassword")}
                    error={errors.newPassword?.message}
                />

                <FormInput
                    label="Xác nhận mật khẩu mới"
                    id="confirmNewPassword"
                    type="password"
                    {...register("confirmNewPassword")}
                    error={errors.confirmNewPassword?.message}
                />

                <FormButton type="submit" disabled={isLoading}>
                    {isLoading ? "Đang xử lý..." : "Đổi mật khẩu"}
                </FormButton>
            </form>
        </div>
    );
}
