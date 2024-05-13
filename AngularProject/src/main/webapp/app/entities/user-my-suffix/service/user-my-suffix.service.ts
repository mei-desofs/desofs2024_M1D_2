import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserMySuffix, NewUserMySuffix } from '../user-my-suffix.model';

export type PartialUpdateUserMySuffix = Partial<IUserMySuffix> & Pick<IUserMySuffix, 'id'>;

export type EntityResponseType = HttpResponse<IUserMySuffix>;
export type EntityArrayResponseType = HttpResponse<IUserMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class UserMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/users');

  create(user: NewUserMySuffix): Observable<EntityResponseType> {
    return this.http.post<IUserMySuffix>(this.resourceUrl, user, { observe: 'response' });
  }

  update(user: IUserMySuffix): Observable<EntityResponseType> {
    return this.http.put<IUserMySuffix>(`${this.resourceUrl}/${this.getUserMySuffixIdentifier(user)}`, user, { observe: 'response' });
  }

  partialUpdate(user: PartialUpdateUserMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IUserMySuffix>(`${this.resourceUrl}/${this.getUserMySuffixIdentifier(user)}`, user, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserMySuffixIdentifier(user: Pick<IUserMySuffix, 'id'>): number {
    return user.id;
  }

  compareUserMySuffix(o1: Pick<IUserMySuffix, 'id'> | null, o2: Pick<IUserMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserMySuffixIdentifier(o1) === this.getUserMySuffixIdentifier(o2) : o1 === o2;
  }

  addUserMySuffixToCollectionIfMissing<Type extends Pick<IUserMySuffix, 'id'>>(
    userCollection: Type[],
    ...usersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const users: Type[] = usersToCheck.filter(isPresent);
    if (users.length > 0) {
      const userCollectionIdentifiers = userCollection.map(userItem => this.getUserMySuffixIdentifier(userItem));
      const usersToAdd = users.filter(userItem => {
        const userIdentifier = this.getUserMySuffixIdentifier(userItem);
        if (userCollectionIdentifiers.includes(userIdentifier)) {
          return false;
        }
        userCollectionIdentifiers.push(userIdentifier);
        return true;
      });
      return [...usersToAdd, ...userCollection];
    }
    return userCollection;
  }
}
