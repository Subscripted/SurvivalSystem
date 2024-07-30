package dev.subscripted.survivalsystem.modules.clans.manager;

import dev.subscripted.survivalsystem.modules.database.MySQL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public class ClanManager {

    final MySQL mySQL;

    public void createClan(UUID ownerUUID, String clanPrefix, String clanName) throws SQLException, ExecutionException, InterruptedException {
        String createClanQuery = "INSERT INTO Clans (ClanPrefix, ClanName, OwnerUUID, ClanLevel) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(createClanQuery)) {
            stmt.setString(1, clanPrefix);
            stmt.setString(2, clanName);
            stmt.setString(3, ownerUUID.toString());
            stmt.setInt(4, 1);
            stmt.executeUpdate();
        }
        addMemberToClan(clanPrefix, ownerUUID);
    }

    public void deleteClan(String clanPrefix) throws SQLException {
        String deleteClanQuery = "DELETE FROM Clans WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(deleteClanQuery)) {
            stmt.setString(1, clanPrefix);
            stmt.executeUpdate();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        removeMembersFromClan(clanPrefix);
    }

    public void addMemberToClan(String clanPrefix, UUID memberUUID) throws SQLException, ExecutionException, InterruptedException {
        String addMemberQuery = "INSERT INTO ClanMembers (ClanPrefix, MemberUUID) VALUES (?, ?)";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(addMemberQuery)) {
            stmt.setString(1, clanPrefix);
            stmt.setString(2, memberUUID.toString());
            stmt.executeUpdate();
        }
    }

    public void removeMemberFromClan(String clanPrefix, UUID memberUUID) throws SQLException, ExecutionException, InterruptedException {
        String removeMemberQuery = "DELETE FROM ClanMembers WHERE ClanPrefix = ? AND MemberUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(removeMemberQuery)) {
            stmt.setString(1, clanPrefix);
            stmt.setString(2, memberUUID.toString());
            stmt.executeUpdate();
        }
    }

    public void leaveClan(UUID memberUUID) throws SQLException, ExecutionException, InterruptedException {
        String leaveClanQuery = "DELETE FROM ClanMembers WHERE MemberUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(leaveClanQuery)) {
            stmt.setString(1, memberUUID.toString());
            stmt.executeUpdate();
        }
    }

    public void removeMembersFromClan(String clanPrefix) throws SQLException {
        String removeMembersQuery = "DELETE FROM ClanMembers WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(removeMembersQuery)) {
            stmt.setString(1, clanPrefix);
            stmt.executeUpdate();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMemberClan(UUID memberUUID) throws SQLException, ExecutionException, InterruptedException {
        String getMemberClanQuery = "SELECT ClanPrefix FROM ClanMembers WHERE MemberUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getMemberClanQuery)) {
            stmt.setString(1, memberUUID.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ClanPrefix");
            }
        }
        return null;
    }

    public boolean isMemberOfClan(UUID memberUUID) throws SQLException {
        String isMemberQuery = "SELECT ClanPrefix FROM ClanMembers WHERE MemberUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(isMemberQuery)) {
            stmt.setString(1, memberUUID.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOwnerOfClan(UUID ownerUUID) throws SQLException {
        String isOwnerQuery = "SELECT ClanPrefix FROM Clans WHERE OwnerUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(isOwnerQuery)) {
            stmt.setString(1, ownerUUID.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UUID> getAllMembersAsList(String clanPrefix) throws SQLException, ExecutionException, InterruptedException {
        List<UUID> members = new ArrayList<>();
        String getMembersQuery = "SELECT MemberUUID FROM ClanMembers WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getMembersQuery)) {
            stmt.setString(1, clanPrefix);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(UUID.fromString(rs.getString("MemberUUID")));
            }
        }
        return members;
    }

    public boolean clanExists(String clanPrefix) throws SQLException, ExecutionException, InterruptedException {
        String clanExistsQuery = "SELECT ClanName FROM Clans WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(clanExistsQuery)) {
            stmt.setString(1, clanPrefix);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean isClanNameTaken(String clanName) throws SQLException {
        String clanNameTakenQuery = "SELECT ClanName FROM Clans WHERE ClanName = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(clanNameTakenQuery)) {
            stmt.setString(1, clanName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClanNameByPrefix(String clanPrefix) throws SQLException {
        String getClanNameQuery = "SELECT ClanName FROM Clans WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getClanNameQuery)) {
            stmt.setString(1, clanPrefix);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ClanName");
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getClanPrefix(UUID memberUUID) throws SQLException, ExecutionException, InterruptedException {
        String getClanPrefixQuery = "SELECT ClanPrefix FROM ClanMembers WHERE MemberUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getClanPrefixQuery)) {
            stmt.setString(1, memberUUID.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ClanPrefix");
            }
        }
        return null;
    }

    public String getClanName(UUID memberUUID) throws SQLException, ExecutionException, InterruptedException {
        String clanPrefix = getClanPrefix(memberUUID);
        if (clanPrefix != null) {
            return getClanNameByPrefix(clanPrefix);
        }
        return null;
    }

    public List<UUID> getClanMembers(String clanPrefix) throws SQLException, ExecutionException, InterruptedException {
        return getAllMembersAsList(clanPrefix);
    }

    public UUID getClanOwner(String clanPrefix) throws SQLException {
        String getClanOwnerQuery = "SELECT OwnerUUID FROM Clans WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getClanOwnerQuery)) {
            stmt.setString(1, clanPrefix);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return UUID.fromString(rs.getString("OwnerUUID"));
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean isClanOwner(UUID ownerUUID) throws SQLException, ExecutionException, InterruptedException {
        String isClanOwnerQuery = "SELECT ClanPrefix FROM Clans WHERE OwnerUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(isClanOwnerQuery)) {
            stmt.setString(1, ownerUUID.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean isClanMember(UUID memberUUID) throws SQLException {
        String isClanMemberQuery = "SELECT ClanPrefix FROM ClanMembers WHERE MemberUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(isClanMemberQuery)) {
            stmt.setString(1, memberUUID.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClan(UUID memberUUID) throws SQLException, ExecutionException, InterruptedException {
        String getClanQuery = "SELECT Clans.ClanPrefix, Clans.ClanName, Clans.OwnerUUID " +
                "FROM Clans JOIN ClanMembers ON Clans.ClanPrefix = ClanMembers.ClanPrefix " +
                "WHERE ClanMembers.MemberUUID = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getClanQuery)) {
            stmt.setString(1, memberUUID.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ClanPrefix") + " - " + rs.getString("ClanName");
            }
        }
        return null;
    }

    @SneakyThrows
    public List<String> getClans() throws SQLException {
        List<String> clans = new ArrayList<>();
        String query = "SELECT ClanPrefix FROM Clans";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String prefix = rs.getString("ClanPrefix");
                clans.add(prefix);
            }
        }

        return clans;
    }

    public int getClanLevel(String clanPrefix) throws SQLException, ExecutionException, InterruptedException {
        String getLevelQuery = "SELECT ClanLevel FROM Clans WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getLevelQuery)) {
            stmt.setString(1, clanPrefix);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ClanLevel");
            }
        }

        return 0;
    }

    public int getClanEco(String clanPrefix) throws SQLException, ExecutionException, InterruptedException {
        String getEcoQuery = "SELECT ClanbankEco FROM Clans WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(getEcoQuery)) {
            stmt.setString(1, clanPrefix);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ClanbankEco");
            }
        }
        return 0;
    }


    public void setClanEco(String clanPrefix, int newEco) throws SQLException, ExecutionException, InterruptedException {
        String setEcoQuery = "UPDATE Clans SET ClanbankEco = ? WHERE ClanPrefix = ?";
        try (PreparedStatement stmt = mySQL.getConnection().get().prepareStatement(setEcoQuery)) {
            stmt.setInt(1, newEco);
            stmt.setString(2, clanPrefix);
            stmt.executeUpdate();
        }
    }
}

