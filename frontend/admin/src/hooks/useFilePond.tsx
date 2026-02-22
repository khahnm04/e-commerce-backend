import { useEffect } from "react";
import * as FilePond from "filepond";

import "filepond/dist/filepond.min.css";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import FilePondPluginFileValidateType from "filepond-plugin-file-validate-type";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";

let isRegistered = false;

export const useFilePond = () => {
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
      })
    );

    return () => {
      instances.forEach(instance => instance.destroy());
    };
  }, []);
};
