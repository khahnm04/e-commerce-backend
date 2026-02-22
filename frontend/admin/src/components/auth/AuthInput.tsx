type AuthInputProps = {
  label: string;
  id: string;
  error?: string;
} & React.InputHTMLAttributes<HTMLInputElement>;

export function AuthInput({
  label,
  id,
  type,
  error,
  ...rest
}: AuthInputProps) {
  return (
    <div className="mb-[24px] relative">
      <label
        htmlFor={id}
        className="block mb-[12px] font-[600] text-[14px] text-[var(--color-text)] opacity-[0.8]"
      >
        {label}
      </label>

      <input
        id={id}
        type={type}
        {...rest}
        className="w-full h-[45px] bg-[#F1F4F9] border border-[#D8D8D8] rounded-[8px] px-[13px] font-[600] text-[14px] text-[var(--color-text)] placeholder:text-[#A6A6A6]"
      />

      {error && (
        <p className="absolute top-[104%] text-[12px] text-[#e63946]">
          {error}
        </p>
      )}
    </div>
  );
}
