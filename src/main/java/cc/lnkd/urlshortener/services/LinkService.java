package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.LinkRequest;
import cc.lnkd.urlshortener.models.LinkResponse;

import java.sql.SQLException;

public interface LinkService {
    LinkResponse retrieveURLFromDB(String slug) throws SQLException;
    LinkResponse writeURLToDBForPaid(LinkRequest request) throws SQLException, BadRequestException;
    LinkResponse writeURLToDBForFree(LinkRequest request) throws SQLException, BadRequestException;
    LinkResponse writeURLToDBForIncognito(LinkRequest request) throws SQLException, BadRequestException;
}
