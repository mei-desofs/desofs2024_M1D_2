import { IPortfolioMySuffix } from 'app/entities/portfolio-my-suffix/portfolio-my-suffix.model';

export interface IUserMySuffix {
  id: number;
  email?: string | null;
  password?: string | null;
  address?: string | null;
  contact?: string | null;
  profilePhoto?: string | null;
  profilePhotoContentType?: string | null;
  portfolio?: Pick<IPortfolioMySuffix, 'id'> | null;
}

export type NewUserMySuffix = Omit<IUserMySuffix, 'id'> & { id: null };
