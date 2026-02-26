import { z } from "zod";

export const CreateRoleSchema = z.object({
  code: z.string().min(1, "Mã nhóm quyền không được để trống"),
  name: z.string().min(1, "Tên nhóm quyền không được để trống"),
  description: z.string().min(1, "Mô tả không được để trống"),
  permissions: z.array(z.number()).min(1, "Vui lòng chọn ít nhất 1 quyền"),
});

export type CreateRoleValues = z.infer<typeof CreateRoleSchema>;
