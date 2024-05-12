import dayjs from 'dayjs';

export interface IPortfolioMySuffix {
  id?: number;
  date?: dayjs.Dayjs | null;
  name?: string | null;
}

export const defaultValue: Readonly<IPortfolioMySuffix> = {};
