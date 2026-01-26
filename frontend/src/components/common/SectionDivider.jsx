import { Divider, Typography } from "@mui/material";

export default function SectionDivider({ label }) {
  return (
    <Divider
      sx={{
        my: 3,
        "&::before, &::after": {
          borderColor: "rgba(25, 118, 210, 0.35)",
        },
      }}
    >
      <Typography
        variant="overline"
        sx={{
          letterSpacing: 2,
          color: "primary.main",
          fontWeight: 600,
        }}
      >
        {label}
      </Typography>
    </Divider>
  );
}
