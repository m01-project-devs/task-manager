import { TableCell, TableRow, TableSortLabel} from "@mui/material"
import { ArrowUpward, ArrowDownward } from "@mui/icons-material";



export default function SortableTableHead({columns, sortConfig, onSort}) {
    const getSortIcon = (field) => {
        if (sortConfig.field === field) {
            return sortConfig.direction === 'asc'
                ? <ArrowUpward fontSize="inherit" />
                : <ArrowDownward fontSize="inherit" />;
        }
        return <ArrowUpward fontSize="inherit" sx={{ opacity: 0.2 }} />;
    };
    const getActive = (field) => sortConfig.field === field;
    return (
        <TableRow>
            {columns.map((column) => (
                <TableCell
                    key={column.field}
                    align={column.align || 'left'}
                    sortDirection={sortConfig.field === column.field ? sortConfig.direction : false}
                    sx={{
                        fontWeight: 'bold',
                        backgroundColor: '#f5f5f5',
                        cursor: column.sortable ? 'pointer' : 'default',
                        '&:hover': column.sortable ? {
                            backgroundColor: '#eeeeee',
                        } : {},

                        pr: column.sortable ? 3 : 1,
                    }}
                >
                    {column.sortable ? (
                        <TableSortLabel
                            active={getActive(column.field)}
                            direction={sortConfig.field === column.field ? sortConfig.direction : 'asc'}
                            onClick={() => onSort(column.field)}
                            IconComponent={() => getSortIcon(column.field)}
                            sx={{
                                '&.Mui-active': {color: 'primary.main',fontWeight: 'bold'},
                                '& .MuiTableSortLabel-icon': {
                                    marginLeft: '4px',
                                    opacity: getActive(column.field) ? 1 : 0.3,
                                },
                                width: '100%',
                                justifyContent: column.align === 'right' ? 'flex-end' : 'flex-start',
                            }}
                        >
                            {column.label}
                        </TableSortLabel>
                    ) : (
                        column.label
                    )}
                </TableCell>
            ))}
        </TableRow>
    )
}