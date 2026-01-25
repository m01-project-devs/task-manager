export const authTextFieldSx = {
  input: { color: "black" },
  label: { color: "#000000" },
  "& label.Mui-focused": {
    color: "#11100f",
  },
  "& .MuiOutlinedInput-root": {
    "& fieldset": {
      borderColor: "#000000",
    },
    "&:hover fieldset": {
      borderColor: "#000000",
    },
    "&.Mui-focused fieldset": {
      borderColor: "#000000",
    },

    // ---------------- AUTOFILL ----------------
    "& input:-webkit-autofill": {
      WebkitBoxShadow: "0 0 0 1000px rgba(2, 162, 255, 0.1) inset !important",
      WebkitTextFillColor: "black !important",
    },
    "& input:-webkit-autofill:hover": {
      WebkitBoxShadow: "0 0 0 1000px rgba(2, 162, 255, 0.1) inset !important",
      WebkitTextFillColor: "black !important",
    },
    "& input:-webkit-autofill:focus": {
      WebkitBoxShadow: "0 0 0 1000px rgba(2, 162, 255, 0.1) inset !important",
      WebkitTextFillColor: "black !important",
    },
  },
};
