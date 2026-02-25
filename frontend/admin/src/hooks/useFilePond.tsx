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
}

export const useFilePond = (options?: UseFilePondOptions) => {
  // Use refs for callbacks to avoid stale closures.
  // The useEffect below runs only once (mount), so without refs,
  // the callbacks would be captured from the initial render and never update.
  const onAddFileRef = useRef(options?.onAddFile);
  const onRemoveFileRef = useRef(options?.onRemoveFile);

  // Keep refs in sync with latest props
  useEffect(() => {
    onAddFileRef.current = options?.onAddFile;
    onRemoveFileRef.current = options?.onRemoveFile;
  });

  useEffect(() => {
    if (!isRegistered) {
      FilePond.registerPlugin(
        FilePondPluginImagePreview,
        FilePondPluginFileValidateType
      );
      isRegistered = true;
    }

    const inputs = document.querySelectorAll<HTMLInputElement>(
      "input.filepond"
    );

    const instances = Array.from(inputs).map(input =>
      FilePond.create(input, {
        allowMultiple: false,
        acceptedFileTypes: ["image/*"],
        labelIdle:
          '<span>+</span>',
        onaddfile: (_error, fileItem) => {
          if (!_error && fileItem?.file && onAddFileRef.current) {
            onAddFileRef.current(fileItem.file as File);
          }
        },
        onremovefile: () => {
          onRemoveFileRef.current?.();
        },
      })
    );

    return () => {
      instances.forEach(instance => instance.destroy());
    };
  }, []);
};
