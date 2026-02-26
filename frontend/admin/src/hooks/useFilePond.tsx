import { useEffect, useRef } from "react";
import * as FilePond from "filepond";

import "filepond/dist/filepond.min.css";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";

let isRegistered = false;

interface UseFilePondOptions {
  onAddFile?: (file: File) => void;
  onRemoveFile?: () => void;
  inputRef?: React.RefObject<HTMLInputElement | null>;
}

export const useFilePond = (options?: UseFilePondOptions) => {
  const onAddFileRef = useRef(options?.onAddFile);
  const onRemoveFileRef = useRef(options?.onRemoveFile);

  useEffect(() => {
    onAddFileRef.current = options?.onAddFile;
    onRemoveFileRef.current = options?.onRemoveFile;
  });

  useEffect(() => {
    if (!isRegistered) {
      FilePond.registerPlugin(FilePondPluginImagePreview, FilePondPluginFileValidateType);
      isRegistered = true;
    }

    const inputs = options?.inputRef?.current
      ? [options.inputRef.current]
      : Array.from(document.querySelectorAll<HTMLInputElement>("input.filepond"));

    const instances = inputs.map((input) =>
      FilePond.create(input, {
        allowMultiple: false,
        acceptedFileTypes: ["image/*"],
        labelIdle: "<span>+</span>",
        onaddfile: (_error, fileItem) => {
          if (!_error && fileItem?.file && onAddFileRef.current) {
            onAddFileRef.current(fileItem.file as File);
          }
        },
        onremovefile: () => {
          onRemoveFileRef.current?.();
        },
      }),
    );

    return () => {
      instances.forEach((instance) => instance.destroy());
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
};
