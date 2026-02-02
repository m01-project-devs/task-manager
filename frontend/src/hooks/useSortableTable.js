import {useState, useCallback} from 'react';


export function useSortableTable(defaultRowsPerPage = 5) {
    const[page, setPage] = useState(0);
    const[rowsPerPage, setRowsPerPage] = useState(defaultRowsPerPage);
    const[sortConfig, setSortConfig] = useState({
        field: null,
        direction: 'asc', // or 'desc'
    });

    const sort = sortConfig.field ? `${sortConfig.field},${sortConfig.direction}` : null;

    const handleChangePage = useCallback((newPage) => {
        setPage(newPage);
    }, []);

    const handleChangeRowsPerPage = useCallback((newRowsPerPage) => {
        setRowsPerPage(newRowsPerPage);
        setPage(0);
    }, [])


    const handleSort = useCallback((field) => {
       setSortConfig((prev) => {
           if(prev.field === field) {
               if(prev.direction === 'asc') {
                   return {field, direction: 'desc'};
               }
               if(prev.direction === 'desc') {
                   return {field: null, direction: 'asc'};
               }
           }

           return {field, direction: 'asc'};
       });
       setPage(0);
    },[]);

    const getSortDirection = useCallback((field) => {
        if(sortConfig.field === field) {
            return sortConfig.direction;
        }
        return null;
    }, [sortConfig]);

    return {
        page,
        rowsPerPage,
        sort,
        sortConfig,
        handleChangePage,
        handleChangeRowsPerPage,
        handleSort,
        getSortDirection
    }
}