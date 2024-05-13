import dayjs from 'dayjs/esm';

export interface IPortfolioMySuffix {
  id: number;
  date?: dayjs.Dayjs | null;
  name?: string | null;
}

export type NewPortfolioMySuffix = Omit<IPortfolioMySuffix, 'id'> & { id: null };
