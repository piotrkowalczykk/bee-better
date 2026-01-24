import React, { useEffect, useRef, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faImage } from "@fortawesome/free-solid-svg-icons";
import classes from "./ImageUploader.module.css";

export const ImageUploader = ({ image, onFileSelect, preview }) => {
  const [imagePreviewUrl, setImagePreviewUrl] = useState(null);
  const inputRef = useRef(null);

  const handleImageChange = (e) => {
    const file = e.target.files[0];

    if (file) {
      onFileSelect(file);

      const reader = new FileReader();
      reader.onloadend = () => setImagePreviewUrl(reader.result);
      reader.readAsDataURL(file);
    }
  };

  useEffect(() => {
  if (image) {
    const reader = new FileReader();
    reader.onloadend = () => setImagePreviewUrl(reader.result);
    reader.readAsDataURL(image);
  } else if (preview) {
    setImagePreviewUrl(preview);
  } else {
    setImagePreviewUrl(null);
    if (inputRef.current) inputRef.current.value = "";
  }
}, [image, preview]);
  return (
    <div className={classes.imageUploaderContainer}>
      <input
        ref={inputRef}
        type="file"
        accept="image/*"
        onChange={handleImageChange}
        className={classes.imageUploaderInput}
      />

      {imagePreviewUrl ? (
        <img
          src={imagePreviewUrl}
          className={classes.imageUploaderImgPreview}
        />
      ) : (
        <FontAwesomeIcon
          icon={faImage}
          className={classes.imageUploaderIcon}
        />
      )}
    </div>
  );
};
