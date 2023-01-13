import { Box, IconButton, useTheme } from "@mui/material";
import { useContext } from "react";
import { ColorModeContext, tokens } from "../../theme";
import InputBase from "@mui/material/InputBase";
import LightModeOutlinedIcon from "@mui/icons-material/LightModeOutlined";
import DarkModeOutlinedIcon from "@mui/icons-material/DarkModeOutlined";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
import SettingsOutlinedIcon from "@mui/icons-material/SettingsOutlined";
import PersonOutlinedIcon from "@mui/icons-material/PersonOutlined";
import SearchIcon from "@mui/icons-material/Search";

const Topbar = () => {
	const theme = useTheme();
	const colors = tokens(theme.palette.mode);
	const colorMode = useContext(ColorModeContext);

	return (
		<Box display="flex" justifyContent="space-between" p={2}>
			{/* SEARCH BAR */}
			<Box
				display="flex"
				backgroundColor="custom.steamColorB"
				borderRadius="3px"
			>
				<InputBase sx={{ marginLeft: 2, flex: 1 }} placeholder="Search"/>
				<IconButton type="button" sx={{ padding: 1, color: "custom.steamColorD" }}>
					<SearchIcon/>
				</IconButton>
			</Box>

			{/* ICONS */}
			<Box display="flex">
				<IconButton onClick={colorMode.toggleColorMode}>
					{theme.palette.mode === "dark" ? (
						<DarkModeOutlinedIcon sx={{ color: "custom.steamColorD" }}/>
					) : (
						<LightModeOutlinedIcon/>
					)}
				</IconButton>
				<IconButton>
					<NotificationsOutlinedIcon sx={{ color: "custom.steamColorD" }}/>
				</IconButton>
				<IconButton>
					<SettingsOutlinedIcon sx={{ color: "custom.steamColorD" }}/>
				</IconButton>
				<IconButton>
					<PersonOutlinedIcon sx={{ color: "custom.steamColorD" }}/>
				</IconButton>
			</Box>
		</Box>
	);
};

export default Topbar;